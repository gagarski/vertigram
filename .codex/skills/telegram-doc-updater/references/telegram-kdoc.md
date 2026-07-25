# Telegram KDoc Guide

Use this guide for both standalone documentation work and documentation included in a Telegram Bot API version bump.

## Source and fidelity

- Treat the official Telegram Bot API documentation as the source of truth.
- Preserve Telegram's wording whenever it agrees with Vertigram's API. Rephrase only when required by Vertigram's
  Kotlin-native representation, a split into multiple classes, or another real difference in the code.
- Do not add assumptions, inferred behavior, or explanatory claims that are absent from both the official
  documentation and the implementation.
- Keep a direct link to the relevant official Telegram documentation in the source class KDoc. Ensure it is also
  preserved in generated documentation.
- Remove HTTP-serialization phrasing that is irrelevant to callers, such as "JSON-serialized object".
- Remove cross-language numeric-storage guidance that is irrelevant once Vertigram exposes a safe Kotlin type. This
  includes significant-bit counts, warnings about integer interpretation in other programming languages, and advice
  to use 64-bit integers or double-precision floats. Preserve any behavioral constraint that shares the same source
  paragraph, such as an identifier being unavailable to the bot.

## Describe the local API

- Resolve Telegram references against Vertigram's local model. For example, link the relevant `Update` subtype or
  property rather than repeating a raw Bot API field name when Vertigram models that field explicitly.
- Link mentioned fields, permissions, administrator rights, business rights, and similar concepts to the corresponding
  local class property when one exists.
- When Telegram documentation refers to a method, link to Vertigram's generated method rather than its request class.
- Describe the Kotlin type actually accepted or returned by Vertigram when it differs from the HTTP API.
- Account for methods split into several request classes. Put each restriction or behavior on the class where it
  applies.

## Put documentation on the declaration it describes

- Keep the method or type overview in class KDoc.
- Put primary-constructor field documentation on the individual properties instead of collecting it in class-level
  `@property` or `@param` tags.
- Put generated-parameter documentation directly on each concrete primary-constructor property. The generator does
  not inherit property KDoc from a base interface or abstract property, so duplicate shared field documentation
  across concrete generated subtypes instead of relying on documentation on their common parent.
- Put enum-value behavior on the corresponding enum entries.
- Link a property mentioned by class documentation when the property itself expresses the concept.
- Do not say that a value is optional when nullability already expresses that fact.
- Do not add "defaults to ..." when the initializer already makes the default visible.

## Wording conventions

- Use the same base wording for every property whose Kotlin type is `ChatId`, regardless of its property name:
  "Unique identifier for the target chat or username of the target bot, supergroup, or channel."
- Change only the role and supported chat kinds when semantics require it. For example, use "source chat" and
  "source bot" for `fromChatId`, and narrow the list when a method supports fewer kinds. Do not retain method-specific
  Telegram variants such as "the chat where the original messages were sent", and do not repeat the raw `@username`
  format.
- Start documentation for mutually exclusive modeled variants with "Case when ...", and combine it with the actual
  field or variant description.
- Use parallel, consistent language for success/approval and failure/rejection cases.
- Do not append explanations merely to make the prose sound more complete.

## Generated documentation and wrappers

- Source KDoc must remain meaningful after it is copied to generated client methods, type constructors, and creators.
- Inspect `VertigramClientGenerator.WRAP_CONFIGS` for wrapper transformations such as `richText`, `richCaption`, and
  `richQuestion`. Document the public generated parameter and place its documentation according to the generated
  signature order.
- When a wrapper combines or replaces source properties, compose its documentation from the applicable source
  documentation without leaking unrelated property text into another `@param` tag.
- Keep generated provenance text, such as `Generated from [...]`, in the descriptive section before parameter tags.
- Wrap generated KDoc at 120 characters while preserving valid continuation lines for `@param` tags.
- Preserve documentation for handwritten methods that bypass normal generation, such as `Telegram.getUpdates`.
- Missing source KDoc must not fail generation; retain the generator's fallback documentation for such declarations.

## Verification

1. Run `.\gradlew.bat :vertigram-telegram-client:compileKotlin --console=plain`.
2. Inspect the affected generated methods, constructors, and creators under `vertigram-telegram-client/build/generated/`.
3. Verify parameter documentation follows the generated signature order and no prose leaks into an adjacent tag.
4. Generate and inspect Dokka output when rendered documentation is part of the request.

Never fix generated output directly. Correct the source KDoc or generator and regenerate it.
