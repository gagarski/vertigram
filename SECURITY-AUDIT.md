# Vertigram Security Audit Report

**Date:** 2026-07-15  
**Scope:** Static review of source code, configuration, CI workflows, and declared dependencies (`gradle/libs.versions.toml`, `gradle/libs.internal.versions.toml`)  
**Repository:** [gagarski/vertigram](https://github.com/gagarski/vertigram)  
**Commit reviewed:** `master` at time of audit

## Executive Summary

No live production secrets (API keys, private keys, tokens) were found committed to the repository. GitHub Actions correctly reference repository secrets via `${{ secrets.* }}` rather than inline values.

However, the audit identified several **security-relevant patterns in application code and documentation**, plus **dependency versions with known published CVEs**. None of these appear to be immediate credential leaks, but they affect secret handling, webhook security, deserialization hardening, and supply-chain posture.

| Severity | Count | Category |
|----------|-------|----------|
| High     | 2     | Dependency CVEs (Jackson) |
| Medium   | 5     | Secret handling, documentation, throttling |
| Low      | 4     | Sample credentials, unmaintained dependency, config persistence |
| Info     | 3     | CI secret usage (correct), placeholder docs |

---

## 1. Code Findings — Secrets & Credential Handling

### 1.1 [Medium] Bot token embedded in Vert.x deployment configuration

`TelegramVerticle.Config` stores the Telegram bot token as a plain `String` and serializes it into Vert.x deployment config JSON:

```134:138:vertigram/src/main/kotlin/ski/gagar/vertigram/verticles/telegram/TelegramVerticle.kt
    data class Config(
        /**
         * Access token
         */
        val token: String,
```

```424:431:vertigram-core/src/main/kotlin/ski/gagar/vertigram/Vertigram.kt
    class DeploymentOptions<Config>(
        vertigram: Vertigram,
        config: Config
    ): io.vertx.core.DeploymentOptions() {
        init {
            @Suppress("DEPRECATION")
            setConfig(VertigramVerticle.ConfigWrapper(vertigram.name, config).toJsonObject(vertigram.objectMapper))
```

**Risk:** The token is persisted in Vert.x `DeploymentOptions` config as cleartext JSON. In clustered deployments, management tooling, or debug dumps, this can expose credentials. Unlike some other sensitive fields in the codebase, `token` has no `@JsonIgnore` or redaction.

**Recommendation:**
- Load tokens from environment variables or a secret manager at runtime.
- Mark `token` with `@JsonIgnore` for serialization paths that do not require it, or use a dedicated `SecretString` / `CharArray` wrapper with custom Jackson handling that never logs or serializes by default.
- Document that deployment config must not be logged or exported.

---

### 1.2 [Medium] Bot token embedded in Telegram API URL path

`TelegramImpl` places the auth token directly in the request URL:

```160:161:vertigram-telegram-client/src/main/kotlin/ski/gagar/vertigram/telegram/client/impl/TelegramImpl.kt
    private fun client(method: String, longPoll: Boolean = false, upload: Boolean = false) =
        client(longPoll, upload).postAbs("${options.tgBase}/bot$token/$method")
```

**Risk:** Tokens in URLs can leak via:
- HTTP access logs (reverse proxies, load balancers)
- Debug/trace logging of request URLs
- Browser/history or crash dumps (less relevant for server-side bots)

This follows Telegram's API design, but applications should ensure logging and proxy configurations strip or redact URL paths.

**Recommendation:** Document operational requirements (disable URL logging on proxies, avoid trace-level HTTP client logging). Consider using Telegram's header-based auth if/when supported for your deployment model.

---

### 1.3 [Medium] Webhook secret token is non-deterministic across restarts

`WebHook` generates a random UUID secret at verticle construction time:

```34:34:vertigram/src/main/kotlin/ski/gagar/vertigram/verticles/telegram/WebHook.kt
    private val secret = UUID.randomUUID()
```

**Risk:** On every restart, a new `secretToken` is registered with Telegram. During rolling deployments or crash recovery, in-flight webhook requests validated against the previous secret will fail (403) until Telegram acknowledges the new webhook. This is an availability concern that can also cause operators to misconfigure workarounds (e.g., disabling secret validation).

**Recommendation:** Allow configuring `secretToken` via `WebHook.Config` (with secure random default), or persist the generated secret across restarts.

---

### 1.4 [Medium] README misdocuments webhook secret header

The module README states that `X-Telegram-Bot-Api-Secret-Token` should be set to the **bot token**:

```148:151:vertigram/README.md
Now we have a web-server listening for updates on port 8080. It's not enough for your webhook to work, you need TLS-enabled
reverse proxy (Vertigram currently does not support TLS termination). If you're up for testing it from your browser,
please note that for security reasons it expects `X-Telegram-Bot-Api-Secret-Token` to be set to your bot token 
(Telegram sets this header). The rest of the ensemble works as before.
```

**Actual behavior:** `WebHook` generates a random UUID, passes it to `setWebhook(secretToken = ...)`, and validates incoming requests against that UUID — **not** the bot token.

**Risk:** Operators following the README may misconfigure reverse proxies or manual testing, potentially weakening webhook authentication or causing confusion during incident response.

**Recommendation:** Update documentation to describe the auto-generated `secretToken` and how to obtain it for local testing (or expose it via config).

---

### 1.5 [Medium] Throttling annotation scan uses wrong package

`ThrottlingTelegram` scans a non-existent/wrong package for `@Throttled` methods:

```202:205:vertigram/src/main/kotlin/ski/gagar/vertigram/telegram/throttling/ThrottlingTelegram.kt
    companion object {
        private val TO_THROTTLE =
            Reflections("ski.gagar.vertigram.types.methods").getTypesAnnotatedWith(Throttled::class.java)
    }
```

Compare with the correct package used elsewhere:

```85:87:vertigram-telegram-client/src/main/kotlin/ski/gagar/vertigram/util/VertigramTypeHints.kt
private fun getTgCallables() =
    Reflections("ski.gagar.vertigram.telegram.types.methods")
        .getTypesAnnotatedWith(TelegramMethod::class.java, true)
```

**Risk:** `@Throttled` methods may never be throttled, increasing exposure to Telegram API rate limits (429) and potential service disruption under load.

**Recommendation:** Fix package to `ski.gagar.vertigram.telegram.types.methods` (or share a constant with `VertigramTypeHints`).

---

### 1.6 [Low] Hardcoded database passwords in sample/test code

| Location | Value | Context |
|----------|-------|---------|
| `vertigram-jooq-app-api/.../DatabaseConfig.kt` | `PASSWORD = "secret"` | TestContainer companion constant |
| `vertigram-jooq/.../DbUtil.kt` | `password = "topsecret"` | `main()` example |
| `vertigram-jooq/README.md` | `password = "topsecret"` | Documentation sample |
| `vertigram-jooq-gradle-plugin/README.md` | `password = "password"` | Documentation sample (with "Better to externalize" note) |

**Risk:** Low for production (clearly sample/test), but copy-paste into real deployments is a common failure mode.

**Recommendation:** Use placeholders like `<from-env:DB_PASSWORD>` in docs; keep testcontainer passwords scoped to ephemeral containers only.

---

### 1.7 [Low] Database credentials held in shared Vert.x data structures

`createSharedDataSource` stores `HikariDataSource` (including password) in Vert.x local shared data:

```20:39:vertigram-jooq/src/main/kotlin/ski/gagar/vertigram/jooq/DbUtil.kt
internal fun Vertx.createSharedDataSource(name: String, jdbcUrl: String, username: String? = null, password: String? = null): DataSource {
    val ds = HikariDataSource().apply dsConstruct@ {
        ...
    }.let { DataSourceWithUrl(it, jdbcUrl) }

    sharedData().getLocalMap<String, ShareableHolder<DataSourceWithUrl>>(DATA_SOURCES_MAP_NAME)
        .compute(name) { _, old ->
```

**Risk:** Passwords remain in JVM heap for the process lifetime. Acceptable for many apps, but worth noting for memory-dump threat models.

---

### 1.8 [Info] Placeholder tokens in documentation only

Examples use obvious placeholders (`"xxx:yyy"`, `"111222333:secrettoken"`, `"xxx"`) in README and serialization tests. No real Telegram token patterns (`<digits>:<35-char-alphanumeric>`) were detected in the repository.

---

## 2. CI / Repository Secret Handling

### 2.1 [Info] GitHub Actions secrets — correctly externalized

`.github/workflows/publish.yml` references secrets without embedding values:

| Secret | Purpose |
|--------|---------|
| `SONATYPE_USERNAME` / `SONATYPE_PASSWORD` | Maven Central publishing |
| `GPG_SIGNING_KEY_ID` / `GPG_SIGNING_KEY` / `GPG_SIGNING_PASSWORD` | Artifact signing |
| `DOKKA_REPO` | Documentation deployment target |
| `DOKKA_SSH_PRIVATE_KEY` / `DOKKA_KNOWN_HOSTS` | SSH docs publishing |

Gradle signing in build scripts reads from project properties (`signingKey`, `signingPassword`), which CI injects via `ORG_GRADLE_PROJECT_*` — correct pattern.

**No committed `.env` files or credential artifacts** (`.pem`, `.key`) were found.

---

## 3. Dependency Findings

Versions from `gradle/libs.versions.toml` and `gradle/libs.internal.versions.toml` as of audit date.

### 3.1 [High] Jackson databind 2.19.4 — multiple 2026 CVEs

**Current version:** `jackson = "2.19.4"` (all Jackson modules)

**Known issues affecting 2.19.x (< 2.21.4):**

| CVE | Severity | Description |
|-----|----------|-------------|
| [CVE-2026-54512](https://github.com/FasterXML/jackson-databind/security/advisories) | High (8.1) | `PolymorphicTypeValidator` bypass via generic container types |
| [CVE-2026-54513](https://github.com/FasterXML/jackson-databind/security/advisories/GHSA-rmj7-2vxq-3g9f) | High | PTV bypass via array component types |
| [CVE-2026-54514](https://github.com/FasterXML/jackson-databind/security/advisories/GHSA-hgj6-7826-r7m5) | Medium | SSRF via eager DNS in `InetSocketAddress` deserialization |
| [CVE-2026-54515](https://guide.sonatype.com/component/maven/com.fasterxml.jackson.core%3Ajackson-databind/2.19.0/vulnerabilities) | Medium | `@JsonIgnoreProperties` bypass in contextual deserializers |

**Patched in:** `2.21.4` / `2.21.5` (2.19 line)

**Vertigram exposure:** Jackson deserializes Telegram updates, verticle configs, and event-bus messages. If untrusted JSON reaches deserialization paths (e.g., malformed webhook payloads, user-controlled config), these CVEs are relevant.

**Recommendation:** Upgrade Jackson to `>= 2.21.5` across the version catalog and run serialization regression tests in `vertigram-telegram-client`.

---

### 3.2 [Low] org.reflections:reflections 0.10.2 — unmaintained

**Used in:** `vertigram` (`ThrottlingTelegram`), `vertigram-telegram-client` (`VertigramTypeHints`)

**Status:** [Officially not under active development](https://github.com/ronmamo/reflections). No direct CVEs on 0.10.2 (Snyk), but:
- Classpath scanning at runtime increases attack surface
- Transitive dependencies include older `javassist`, `slf4j-api 1.7.32` in reflections' POM

**Recommendation:** Replace runtime classpath scanning with KSP/codegen (already used elsewhere in the project) or compile-time annotation indexing.

---

### 3.3 [Info] Dependencies in good standing

| Dependency | Version | Notes |
|------------|---------|-------|
| logback-classic | 1.5.37 | Fixes CVE-2026-13006; Janino conditional processing removed |
| postgresql JDBC | 42.7.12 | Current stable line |
| vertx | 5.1.3 | Recent major line |
| kotlin | 2.4.0 | Current |
| flyway | 12.10.0 | Current |
| commons-lang3 | 3.20.0 | Current |
| HikariCP | 7.1.0 | Current |
| testcontainers | 1.21.4 | Current |

---

## 4. Deserialization & Input Handling

### 4.1 [Info] Jackson polymorphic typing in Vertigram

The framework uses Jackson type deduction (`AsPropertyTypeWithDeductionDeserializer`, `@JsonTypeInfo`) for configuration and Telegram types. This is safer than default typing, but still requires vigilance when deserializing partially trusted input.

**Recommendation:** Audit all endpoints that deserialize external JSON (webhooks, event bus in clustered mode). Ensure `PolymorphicTypeValidator` is configured if enabling default typing anywhere.

### 4.2 [Info] Webhook malformed-update handling

`WebHook` returns HTTP 200 for malformed JSON to avoid Telegram retry storms. This is a deliberate availability trade-off; it does not directly leak secrets but may complicate attack detection.

---

## 5. Recommended Actions (Priority Order)

1. **Upgrade Jackson to >= 2.21.5** and re-run `vertigram-telegram-client` serialization tests.
2. **Fix `ThrottlingTelegram` Reflections package** so `@Throttled` methods are actually throttled.
3. **Correct webhook secret documentation** in `vertigram/README.md`.
4. **Harden token handling:** externalize bot tokens; avoid cleartext in deployment config where possible.
5. **Make webhook `secretToken` configurable/persistent** across restarts.
6. **Plan migration off `org.reflections`** to compile-time discovery.
7. **Add automated dependency scanning** (Dependabot, OWASP Dependency-Check, or Gradle `dependencyUpdates` + advisory DB) to CI.

---

## 6. Methodology

- Pattern search for API keys, tokens, passwords, private keys, and `.env` files
- Manual review of verticle configuration, Telegram client, jOOQ modules, and CI workflows
- Cross-reference of declared dependency versions against public CVE databases (NVD, GitHub Security Advisories, Snyk) as of 2026-07-15
- Build verification was limited by JVM 25 requirement in the audit environment (Java 21 available); dependency versions were reviewed from version catalogs

---

*This report is informational. It does not constitute a penetration test or formal certification.*
