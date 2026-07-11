---
name: vertigram-telegram-api-bump
description: Update Vertigram's vertigram-telegram-client module for a requested Telegram Bot API version. Use when the user asks to bump supported Telegram Bot API versions, apply Telegram Bot API changelog entries, add Telegram API types/methods/json body definitions, update Telegram markup builders, or add serialization tests/docs/samples for Bot API changes in this repository.
---

# Vertigram Telegram API Bump

## Core Rule

Only update to the exact Telegram Bot API version the user requested. Do not apply later changelog entries unless the user explicitly asks for the next version.

## Workflow

1. Read `AGENTS.md`, `vertigram-telegram-client/build.gradle.kts`, and `vertigram-telegram-client/README.md` before editing.
2. Browse the official Telegram Bot API changelog and docs for the requested version:
   - Changelog: `https://core.telegram.org/bots/api-changelog`
   - Current docs: `https://core.telegram.org/bots/api`
3. Read the surrounding local files before implementing: types under `vertigram-telegram-client/src/main/kotlin/ski/gagar/vertigram/telegram/types`, methods under `types/methods`, markup builders under `telegram/markup`, samples under `ski/gagar/vertigram/samples`, and nearby tests.
4. Implement only the requested version's additions/changes, following existing Kotlin, Jackson, and `@TelegramCodegen.Type` conventions.
5. Add focused serialization tests for new methods, new types, and every polymorphic subtype introduced.
6. Update markup builders, samples, and the module README when public behavior or public DSL usage changes.
7. Verify with the narrowest relevant Gradle task, then prefer `.\gradlew.bat :vertigram-telegram-client:clean :vertigram-telegram-client:test --console=plain` before finishing broad API changes.

## Detailed Reference

Read [references/version-bump-checklist.md](references/version-bump-checklist.md) when actively performing a version bump or reviewing one.
