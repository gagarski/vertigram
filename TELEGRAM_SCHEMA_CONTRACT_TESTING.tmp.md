# Telegram schema contract testing

## Goal

Test Vertigram's Telegram serialization boundary without depending on real Telegram
state or behavior. The tests should detect structural incompatibilities between
Vertigram and the Telegram Bot API:

- incorrect request parameter names;
- omitted or unexpectedly included values, especially `false`, `0`, empty values,
  and `null`;
- incorrect encoding of primitives, nested objects, lists, unions, and enums;
- multipart and `attach://` reference errors;
- response properties or polymorphic variants that Vertigram cannot deserialize;
- incorrect method-to-response type hints.

This is deliberately not intended to validate Telegram behavior such as whether a
message exists, a bot can access a chat, or a method is allowed in the current
conversation state.

## Schema source

Use `@gramio/schema-parser` only to obtain and parse the upstream Bot API schema.
Convert its output into a pinned representation consumed by the Kotlin tests.

The schema must remain an independent oracle. Vertigram production code and the
contract-test server must not share transformations for field naming, optionality,
or union handling. Otherwise the same transformation error could occur on both
sides and pass the test.

## Architecture

All runtime components can be implemented in Kotlin with Vert.x:

```text
@gramio/schema-parser
        |
        v
Pinned Telegram schema
        |
        +--> Fake Telegram server
        |      - validates raw JSON and multipart requests
        |      - generates schema-compliant responses
        |
        +--> Coverage model
               - required/optional fields
               - union variants
               - minimum/maximum response shapes

Random TelegramCallable instance
        |
        v
Telegram.call(callable)
        |
        v
Vertigram's real JSON/multipart/HTTP implementation
        |
        v
Fake Telegram server
        |
        v
Schema-generated response
        |
        v
Vertigram's real response deserializer
```

The uniform test entry point is:

```kotlin
@Suppress("DEPRECATION")
val result = telegram.call(callable)
```

There is no need to invoke every generated suspend convenience function through
reflection. Reflection is needed only to construct concrete
`TelegramCallable<*>` data classes.

`VertigramTypeHints.descriptorByCallable` already provides the registry of
callable runtime classes and their response `JavaType`s. It should be used instead
of classpath scanning.

## Request generation

Select a concrete callable class from the type-hint registry, inspect its primary
constructor, recursively generate its arguments, and invoke it with
`KFunction.callBy`.

The generator needs explicit support for:

- primitive values and strings;
- nullable types;
- enums;
- collections and maps;
- Kotlin data classes;
- sealed classes and other unions;
- wrapper/value types such as `ChatId`;
- `Attachment` implementations;
- recursion with a depth budget.

Special generators should be registered by `KClass` for types whose valid values
cannot be derived generically:

```kotlin
val overrides: Map<KClass<*>, Arb<*>> = mapOf(
    ChatId::class to chatIdArb,
    Attachment::class to attachmentArb,
    FormattedText::class to formattedTextArb
)
```

Calling the callable data class directly tests serialization, HTTP construction,
multipart encoding, response deserialization, and runtime response-type lookup. It
does not test the generated convenience functions that construct callable
objects. Those wrappers can have a smaller separate mapping suite.

## Directed and random coverage

Pure randomness is insufficient. Every method and object type should receive
directed cases in addition to seeded random cases:

1. Minimal: only required or non-nullable values.
2. Maximal: all optional fields populated.
3. Falsy: `false`, `0`, empty strings, and empty collections where valid.
4. Nullability: absent and explicit `null` where the transport supports the
   distinction.
5. Boundaries: documented minimum and maximum numeric and collection sizes.
6. Text: Unicode, escaping, control characters, and long values.
7. Unions: every concrete subtype or discriminator value.
8. Collections: empty, singleton, and multiple elements.
9. Seeded random combinations.

Failures must record at least:

- random seed;
- method name;
- generated callable;
- raw HTTP request;
- raw HTTP response;
- validation or deserialization error.

The seed and method should be accepted as test parameters so that CI failures are
exactly reproducible.

## Fake-server request validation

The server must inspect the raw wire request rather than a representation produced
by Vertigram code.

For JSON requests it should:

- distinguish missing, `null`, and falsy values;
- reject unknown properties;
- require all schema-required properties;
- validate primitives, arrays, nested objects, enums, and unions;
- produce errors containing the method and JSON path.

For multipart requests it should:

- parse and validate all ordinary fields;
- validate JSON stored inside text parts;
- find every `attach://name` reference;
- require exactly one file part for every reference;
- reject unused or duplicate file parts;
- consume streaming parts;
- optionally verify generated content using a size and checksum;
- preserve filenames and content types for assertions where relevant.

The server should not try to emulate users, chats, messages, permissions, or other
Telegram state.

## Response generation and verification

The fake server determines the result schema for the requested method and
generates a compliant JSON value. Generation should have minimal, maximal, subtype
coverage, and seeded-random modes.

Generated values should be distinctive per property rather than repeatedly using
generic values such as `"test"`. This makes swapped or incorrectly mapped
properties observable:

```json
{
  "message_id": 812341,
  "text": "Message.text_8392",
  "chat": {
    "id": 981723,
    "type": "private",
    "first_name": "Chat.first_name_123"
  }
}
```

Recursive structures need an explicit depth budget and a terminating minimal form.

After `Telegram.call` returns, serialize the Kotlin result to a Jackson `JsonNode`
and compare it structurally with the generated response. The comparison may need
normalization for intentional asymmetry such as omitted `null` values, ignored
forward-compatible properties, or custom wrapper representations.

Minimal responses detect properties that Vertigram incorrectly considers
mandatory. Maximal responses detect missing mappings and unsupported nested
types.

## Suggested libraries

### Kotest Property

Use Kotest Property as the central generation and execution framework:

- deterministic seeds;
- edge cases;
- shrinking;
- `Arb` composition;
- suspend-compatible property tests.

Kotest reflective Arbs can help with ordinary data classes, primitives, enums,
sealed classes, and collections. A small dynamic `KType -> Arb<Any?>` adapter will
still be needed because callable types are selected at runtime.

Prefer composed Arbs, such as `Arb.bind`, where possible. A monolithic
`arbitrary { reflectivelyConstruct() }` generator will not automatically shrink
individual fields well.

Documentation:

- https://kotest.io/docs/proptest/reflective-arbs.html
- https://kotest.io/docs/proptest/property-test-shrinking.html
- https://kotest.io/docs/proptest/property-test-seeds.html

### Kotlin reflection

Use `kotlin-reflect` for primary constructors, `KType` inspection, nullability,
generic type arguments, and `callBy`.

Keep reflective construction in the `vertigram-telegram-client` test source set so
that internal callable constructors remain accessible.

### Fixture Monkey

Fixture Monkey is worth a short prototype. Its Kotlin plugin uses primary
constructors and it has Jackson and Kotest integrations:

- https://naver.github.io/fixture-monkey/v1-0-0/docs/get-started/creating-objects-in-kotlin/

Prototype it against difficult method and value types before adopting it:

- `SendMessage`;
- `SendMediaGroup`;
- `EditMessageText`;
- `AnswerInlineQuery`;
- a sticker method;
- a business method;
- a multipart method;
- `ChatId`;
- nested reply markup;
- `GetUpdates`.

If its customization model becomes harder than maintaining the Telegram-specific
rules directly, retain Kotest but replace Fixture Monkey with the recursive
`KType -> Arb` generator.

### Jackson

Use the existing Jackson configuration and `JsonNode` tree model for generated
responses, schema validation input, and normalized comparisons.

### JsonUnit

JsonUnit can provide clearer path-level differences than a raw
`JsonNode.equals()` failure for large nested Telegram objects:

- https://github.com/lukas-krecan/JsonUnit

### NetworkNT JSON Schema Validator

If the fetched Telegram schema is mechanically converted to standard JSON Schema,
NetworkNT can provide mature Jackson-based validation and useful instance/schema
locations in errors:

- https://github.com/networknt/json-schema-validator

It validates but does not generate instances. Response generation still requires
the schema visitor.

Using it is optional. Direct validation against the Telegram schema representation
may be simpler and avoids adding a schema-to-JSON-Schema translation that could
itself contain mistakes.

### Vert.x Web

Use Vert.x Web for the fake endpoint, request-body handling, routing by Telegram
method name, and multipart processing. A separate Node service or Testcontainer is
not required at runtime.

## What the suite proves

The suite provides strong evidence that:

- Vertigram constructs a wire request accepted by the independent Telegram schema;
- JSON and multipart encoding preserve the intended callable values;
- Vertigram can deserialize schema-compliant method results;
- every registered callable has a valid method and response-type association.

It does not prove:

- that Telegram accepts the request in a particular state;
- cross-field semantic rules absent from the schema;
- permissions or entity existence;
- behavioral correspondence between the request and generated response;
- that Telegram's implementation exactly matches the fetched schema.

Those are acceptable limitations because this suite targets serialization rather
than Telegram integration behavior.

## Initial implementation sequence

1. Pin the parsed schema in a Kotlin-friendly serialized representation.
2. Implement lookup by method name and schema type name.
3. Implement the schema validator for JSON requests.
4. Implement the recursive schema-to-`Arb<JsonNode>` response generator.
5. Implement the reflective `KType -> Arb` callable generator.
6. Start a Vert.x fake server on a random local port.
7. Run `Telegram.call` against it for JSON-only methods.
8. Add minimal, maximal, falsy, subtype, and seeded-random coverage modes.
9. Add structural response comparison and failure artifacts.
10. Add multipart and streaming attachment validation.
11. Add a small separate suite for generated convenience-function mappings.

