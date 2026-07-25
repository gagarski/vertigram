---
name: telegram-doc-updater
description: Update and review Vertigram Telegram Bot API KDoc for source methods, types, constructor properties, generated client methods, creators, and wrappers. Use when the user asks to add, rewrite, audit, or regenerate Telegram API documentation without performing a Bot API version bump.
---

# Telegram Doc Updater

## Workflow

1. Read the repository instructions, `vertigram-telegram-client/build.gradle.kts`, and
   `vertigram-telegram-client/README.md`.
2. Read [the Telegram KDoc guide](references/telegram-kdoc.md) completely before editing documentation.
3. Read the relevant sections of the official Telegram Bot API documentation at
   `https://core.telegram.org/bots/api`.
4. Inspect the source classes, referenced local model types, nearby reviewed KDoc, generated signatures, and
   `WRAP_CONFIGS` entries that affect the requested scope.
5. Edit source files only. Never edit generated files under `build/`.
6. Run `.\gradlew.bat :vertigram-telegram-client:compileKotlin --console=plain`, then inspect the generated KDoc.
   Run Dokka as well when the user requests rendered documentation.

Stay within the requested files, methods, types, or alphabetical range. Pause for review when requested, and do not
silently change API behavior while updating documentation.
