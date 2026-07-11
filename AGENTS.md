# AGENTS.md

## Project Overview

Vertigram is a multi-module Gradle project for building Telegram bots with Vert.x, Kotlin, and coroutines. The root project is `vertigram-all`; published artifacts use the `ski.gagar.vertigram` group.

Key modules:

- `vertigram`: main bot framework APIs and verticles.
- `vertigram-core`: shared Vert.x/Jackson/Kotlin core utilities.
- `vertigram-telegram-client`: Telegram API types, methods, serialization tests, and KSP-generated client code.
- `vertigram-codegen`: KSP code generators used by `vertigram-telegram-client`.
- `vertigram-annotations`: annotations consumed by code generation.
- `vertigram-util`: shared utility code.
- `vertigram-jooq`, `vertigram-jooq-app`, `vertigram-jooq-app-api`, `vertigram-jooq-gradle-plugin`: jOOQ/database integration and Gradle plugin support.
- `vertigram-logback`: Logback integration.
- `vertigram-docs`, `vertigram-dokka-tool`: documentation tooling.
- `vertigram-bom`, `vertigram-version-catalog`: dependency management artifacts.

## Environment

- Use the Gradle wrapper: `.\gradlew.bat` on Windows or `./gradlew` on Unix-like shells.
- Java target/source compatibility is 25.
- Kotlin language version is 2.4 and JVM target is 25.
- Tests use JUnit Platform / JUnit Jupiter.
- Dependency versions live in `gradle/libs.versions.toml` and `gradle/libs.internal.versions.toml`.
- Build conventions are in `buildSrc/src/main/kotlin`.

## Common Commands

From the repository root:

```powershell
.\gradlew.bat build
.\gradlew.bat test
.\gradlew.bat :vertigram-telegram-client:test
.\gradlew.bat :vertigram-telegram-client:kspKotlin
.\gradlew.bat dokkaGenerate
```

For a focused module change, prefer the relevant module task first, then run broader verification if behavior spans modules.

## Coding Guidelines

- Follow the existing Kotlin style and package structure under `ski.gagar.vertigram`.
- Keep public APIs stable unless the task explicitly asks for an API change.
- Prefer existing build convention plugins over duplicating Gradle configuration in individual modules.
- Add dependencies through the version catalogs rather than hard-coding versions in module build files.
- Keep coroutine and Vert.x code non-blocking unless an existing API already establishes a blocking boundary.
- Preserve Jackson serialization compatibility in Telegram API types; serialization tests in `vertigram-telegram-client` are important regression coverage.
- Do not manually edit generated KSP output under `build/`. Change annotations, source models, or `vertigram-codegen` instead.

## Testing Notes

- Run the narrowest relevant test task while iterating.
- Run `.\gradlew.bat test` before finishing broad changes.
- Run `.\gradlew.bat build` when changes affect build logic, publishing metadata, generated sources, or multiple modules.
- jOOQ and Gradle plugin changes may involve integration behavior; check the specific module tasks and surrounding README files before assuming unit tests are enough.

## Documentation

- Module READMEs are used as Dokka includes. Update the corresponding README when changing public behavior or usage examples.
- Dokka configuration suppresses `internal` and `samples` packages from public docs.
- Sample code used by docs commonly lives at `src/main/kotlin/ski/gagar/vertigram/samples/Samples.kt`.

## Release And Publishing

- Release configuration requires the `master` branch.
- Publishing metadata and signing are configured through `buildlogic.maven-publishing-convention`.
- The root `afterReleaseBuild` task wires publishing and Sonatype close/release behavior. Do not change release tasks casually.

## Agent Workflow

- Inspect the relevant module build file and README before editing a module.
- Keep changes scoped; this repository has many published artifacts, so small API or dependency changes can have broad effects.
- Be careful with generated code, publication metadata, and version catalogs.
- If tests cannot be run locally, state exactly which command was skipped or failed and why.
