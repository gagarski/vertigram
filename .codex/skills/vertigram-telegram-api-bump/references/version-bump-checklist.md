# Vertigram Telegram Bot API Version Bump Checklist

## Scope Control

- Confirm the exact target version in the user request.
- Compare only the changelog section for that version against the local code.
- If a changelog item belongs to another API surface such as Telegram Web Apps JavaScript and the user excludes it, leave it out.
- If docs and changelog disagree, mention the discrepancy and prefer current official docs for field shape and type details.

## Local Conventions

- Prefer existing type locations and naming patterns over new package structures.
- Use `@TelegramCodegen.Type` and companion objects consistently with neighboring Telegram types.
- Preserve Jackson polymorphism patterns:
  - `@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)`
  - `@JsonSubTypes` entries for every concrete subtype.
  - Enum constants annotated with `@JsonProperty(CONST_STR)` and string constants in companion objects when that pattern exists nearby.
- Avoid `Any` in public Telegram models. Model Telegram unions as sealed interfaces/classes, explicit wrapper value types, or custom serializers when Telegram JSON permits multiple shapes.
- Prefer nested objects/classes when the user asks for API grouping, e.g. `SuggestedPost.Price`, `ChatOwner.Left`.
- Keep constructors internal and rely on generated creators when that is the existing pattern.
- For builder methods with multiple user-facing arguments, consider the repository's `NoPosArgs` positional-argument guard if surrounding builders use it.

## Methods And Request Bodies

- Add new Bot API methods under `types/methods`.
- Include the correct chat id type (`ChatId` vs `Long`) by checking similar methods and docs.
- Add optional fields to existing methods where the changelog adds parameters.
- For mutually exclusive request bodies, prefer explicit subclasses plus Jackson polymorphism over runtime validation of nullable fields.

## Rich Text And Markup Builders

- Keep regular message builders and rich-message builders stylistically aligned.
- Use Kotlin DSL classes rather than bare strings for public builder concepts when practical.
- Reuse existing escaping utilities where output rules match. Keep separate HTML escaping only for embedded custom HTML snippets that are not emitted through `kotlinx-html`.
- Prefer element inheritance/render methods over large dispatcher `when` blocks for rich-message builder internals.
- Use `kotlinx-html` for HTML rendering when possible; only use custom tags for Telegram-specific tags not modeled by the library.
- Support both HTML and Markdown builder output if the analogous regular-message builder supports both.

## Tests

- Add or update tests in `vertigram-telegram-client/src/test/kotlin`.
- For each new polymorphic type, assert every subtype survives serialization.
- For method tests that accept polymorphic fields, exercise every introduced subtype, not only one representative.
- For custom serializers/deserializers, include nested/composite values if Telegram JSON allows string/list/object variants.
- For methods with attachments, keep existing `skip = setOf(Companion.Mappers.TELEGRAM)` patterns when deserialization is unsupported.

## Docs And Samples

- Update `vertigram-telegram-client/README.md` for new public workflows.
- Add sample functions in `src/main/kotlin/ski/gagar/vertigram/samples/Samples.kt` when adding public builders or notable APIs.
- Reference samples from KDoc with `@sample` when matching existing style.

## Verification

- Start with focused tasks such as:
  - `.\gradlew.bat :vertigram-telegram-client:compileKotlin --console=plain`
  - `.\gradlew.bat :vertigram-telegram-client:test --tests <TestClass> --console=plain`
- For broad API changes, run:
  - `.\gradlew.bat :vertigram-telegram-client:clean :vertigram-telegram-client:test --console=plain`
- If non-clean test compilation reports many unrelated unresolved generated `.create` functions, suspect stale KSP output. A clean module test should be used for verification.
- Do not edit generated KSP output under `build/`. Change annotations, source models, or `vertigram-codegen` instead.

## KSP Build Note

KSP normally wires generated Kotlin sources into compilation. Avoid manually adding `build/generated/ksp/main/kotlin` to Kotlin source sets unless a concrete regression proves it is needed. Manual source registration can contribute to stale generated-symbol behavior.
