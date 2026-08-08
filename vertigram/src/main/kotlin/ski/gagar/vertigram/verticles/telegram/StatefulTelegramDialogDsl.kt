package ski.gagar.vertigram.verticles.telegram

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import java.lang.reflect.Type
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.util.jackson.typeReference
import ski.gagar.vertigram.verticles.telegram.StatefulTelegramDialogVerticle.HistoryBehavior

@DslMarker
annotation class StatefulDialogDsl

/**
 * A reusable definition which creates a fresh stateful dialog verticle for every deployment.
 *
 * This is part of a prototype API whose shape and continued availability are not yet stable. The prototype
 * currently models regular, message-only states with a limited action surface. It does not expose callback-query
 * handling, ephemeral states, timers, rollback, or the complete state lifecycle API. Prefer subclassing
 * [StatefulTelegramDialogVerticle] for production dialogs or when the full framework API is required.
 *
 * Further development is intentionally deferred until integration with a Dispatch DSL demonstrates enough value.
 */
class DialogDefinition<Config> internal constructor(
    internal val configType: Type,
    internal val token: Any,
    internal val chatIdProvider: (Config) -> Long,
    internal val initialProvider: DialogInitialScope<Config>.() -> DialogStateTarget<Config>
) {
    fun createVerticle(): StatefulTelegramDialogVerticle<Config> = DslDialogVerticle(this)
}

/**
 * Build a reusable, regular-state, message-only dialog definition.
 *
 * This is a prototype, API-unstable alternative to subclassing [StatefulTelegramDialogVerticle]. Its current scope
 * is intentionally limited; see [DialogDefinition].
 */
inline fun <reified Config> dialog(
    noinline block: DialogBuilder<Config>.() -> Unit
): DialogDefinition<Config> = dialog(typeReference<Config>(), block)

/**
 * Build a prototype dialog from an explicit config type, for use from non-reified generic code.
 *
 * This overload has the same unstable status and limited scope as [DialogDefinition].
 */
fun <Config> dialog(
    configType: TypeReference<Config>,
    block: DialogBuilder<Config>.() -> Unit
): DialogDefinition<Config> = DialogBuilder<Config>().apply(block).build(configType.type)

/**
 * Builder for the prototype regular, message-only dialog DSL.
 *
 * Prefer the subclass-based [StatefulTelegramDialogVerticle] API when stability or the complete state API matters.
 */
@StatefulDialogDsl
class DialogBuilder<Config> internal constructor() {
    private val token = Any()
    private val definitions = mutableListOf<StateDefinitionBase<Config>>()
    private var chatIdProvider: ((Config) -> Long)? = null
    private var initialProvider: (DialogInitialScope<Config>.() -> DialogStateTarget<Config>)? = null
    private var built = false

    fun chatId(provider: (Config) -> Long) {
        check(chatIdProvider == null) { "chatId is already defined" }
        chatIdProvider = provider
    }

    fun initial(provider: DialogInitialScope<Config>.() -> DialogStateTarget<Config>) {
        check(initialProvider == null) { "initial state is already defined" }
        initialProvider = provider
    }

    fun state(): StateDefinition0<Config> = register(StateDefinition0(this, token))

    fun <A> state(
        @Suppress("UNUSED_PARAMETER") arity: StateArity.One = StateArity.One
    ): StateDefinition1<Config, A> = register(StateDefinition1(this, token))

    fun <A, B> state(
        @Suppress("UNUSED_PARAMETER") arity: StateArity.Two = StateArity.Two
    ): StateDefinition2<Config, A, B> = register(StateDefinition2(this, token))

    fun <A, B, C> state(
        @Suppress("UNUSED_PARAMETER") arity: StateArity.Three = StateArity.Three
    ): StateDefinition3<Config, A, B, C> = register(StateDefinition3(this, token))

    fun <A, B, C, D> state(
        @Suppress("UNUSED_PARAMETER") arity: StateArity.Four = StateArity.Four
    ): StateDefinition4<Config, A, B, C, D> = register(StateDefinition4(this, token))

    fun <A, B, C, D, E> state(
        @Suppress("UNUSED_PARAMETER") arity: StateArity.Five = StateArity.Five
    ): StateDefinition5<Config, A, B, C, D, E> = register(StateDefinition5(this, token))

    internal fun ensureMutable() = check(!built) { "dialog definition is already built" }

    private fun <T : StateDefinitionBase<Config>> register(definition: T): T {
        ensureMutable()
        definitions += definition
        return definition
    }

    internal fun build(configType: Type): DialogDefinition<Config> {
        ensureMutable()
        built = true
        check(definitions.isNotEmpty()) { "dialog must define at least one state" }
        definitions.forEach {
            check(it.isDefined) { "state ${it.displayName} is not defined" }
        }
        return DialogDefinition(
            configType = configType,
            token = token,
            chatIdProvider = requireNotNull(chatIdProvider) { "chatId is not defined" },
            initialProvider = requireNotNull(initialProvider) { "initial state is not defined" }
        )
    }
}

/** Defaulted marker parameters let Kotlin distinguish state factory arities while keeping calls parameterless. */
sealed class StateArity private constructor() {
    data object One : StateArity()
    data object Two : StateArity()
    data object Three : StateArity()
    data object Four : StateArity()
    data object Five : StateArity()
}

@StatefulDialogDsl
class DialogInitialScope<Config> internal constructor(val config: Config)

class DialogStateTarget<Config> internal constructor(
    internal val definition: StateDefinitionBase<Config>,
    internal val arguments: List<Any?>,
    internal val configure: StateBuilder<Config>.() -> Unit
)

abstract class StateDefinitionBase<Config> internal constructor(
    private val owner: DialogBuilder<Config>,
    internal val token: Any
) {
    private var name: String? = null
    internal var isDefined: Boolean = false
        private set

    internal val displayName: String get() = name ?: "<unnamed>"

    protected fun bind(property: KProperty<*>) {
        val current = name
        check(current == null || current == property.name) { "state is already named $current" }
        name = property.name
    }

    protected fun define() {
        owner.ensureMutable()
        check(!isDefined) { "state $displayName is already defined" }
        isDefined = true
    }

    protected fun target(arguments: List<Any?>, configure: StateBuilder<Config>.() -> Unit): DialogStateTarget<Config> =
        DialogStateTarget(this, arguments, configure)
}

class StateDefinition0<Config> internal constructor(owner: DialogBuilder<Config>, token: Any) :
    StateDefinitionBase<Config>(owner, token), ReadOnlyProperty<Any?, StateDefinition0<Config>> {
    private var block: (StateBuilder<Config>.() -> Unit)? = null
    fun define(block: StateBuilder<Config>.() -> Unit) { define(); this.block = block }
    operator fun invoke() = target(emptyList()) { requireNotNull(block) { "state $displayName is not defined" }() }
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = apply { bind(property) }
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = this
}

class StateDefinition1<Config, A> internal constructor(owner: DialogBuilder<Config>, token: Any) :
    StateDefinitionBase<Config>(owner, token), ReadOnlyProperty<Any?, StateDefinition1<Config, A>> {
    private var block: (StateBuilder<Config>.(A) -> Unit)? = null
    fun define(block: StateBuilder<Config>.(A) -> Unit) { define(); this.block = block }
    operator fun invoke(a: A) = target(listOf(a)) { requireNotNull(block) { "state $displayName is not defined" }(a) }
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = apply { bind(property) }
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = this
}

class StateDefinition2<Config, A, B> internal constructor(owner: DialogBuilder<Config>, token: Any) :
    StateDefinitionBase<Config>(owner, token), ReadOnlyProperty<Any?, StateDefinition2<Config, A, B>> {
    private var block: (StateBuilder<Config>.(A, B) -> Unit)? = null
    fun define(block: StateBuilder<Config>.(A, B) -> Unit) { define(); this.block = block }
    operator fun invoke(a: A, b: B) = target(listOf(a, b)) { requireNotNull(block)(a, b) }
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = apply { bind(property) }
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = this
}

class StateDefinition3<Config, A, B, C> internal constructor(owner: DialogBuilder<Config>, token: Any) :
    StateDefinitionBase<Config>(owner, token), ReadOnlyProperty<Any?, StateDefinition3<Config, A, B, C>> {
    private var block: (StateBuilder<Config>.(A, B, C) -> Unit)? = null
    fun define(block: StateBuilder<Config>.(A, B, C) -> Unit) { define(); this.block = block }
    operator fun invoke(a: A, b: B, c: C) = target(listOf(a, b, c)) { requireNotNull(block)(a, b, c) }
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = apply { bind(property) }
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = this
}

class StateDefinition4<Config, A, B, C, D> internal constructor(owner: DialogBuilder<Config>, token: Any) :
    StateDefinitionBase<Config>(owner, token), ReadOnlyProperty<Any?, StateDefinition4<Config, A, B, C, D>> {
    private var block: (StateBuilder<Config>.(A, B, C, D) -> Unit)? = null
    fun define(block: StateBuilder<Config>.(A, B, C, D) -> Unit) { define(); this.block = block }
    operator fun invoke(a: A, b: B, c: C, d: D) = target(listOf(a, b, c, d)) { requireNotNull(block)(a, b, c, d) }
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = apply { bind(property) }
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = this
}

class StateDefinition5<Config, A, B, C, D, E> internal constructor(owner: DialogBuilder<Config>, token: Any) :
    StateDefinitionBase<Config>(owner, token), ReadOnlyProperty<Any?, StateDefinition5<Config, A, B, C, D, E>> {
    private var block: (StateBuilder<Config>.(A, B, C, D, E) -> Unit)? = null
    fun define(block: StateBuilder<Config>.(A, B, C, D, E) -> Unit) { define(); this.block = block }
    operator fun invoke(a: A, b: B, c: C, d: D, e: E) = target(listOf(a, b, c, d, e)) {
        requireNotNull(block)(a, b, c, d, e)
    }
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = apply { bind(property) }
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = this
}

@StatefulDialogDsl
class StateBuilder<Config> internal constructor() {
    internal val filters = mutableListOf<suspend MessageFilterScope<Config>.(Message) -> Boolean>()
    internal val sideEffects = mutableListOf<suspend StateActionScope<Config>.() -> Unit>()
    internal val handlers = mutableListOf<suspend StateActionScope<Config>.(Message) -> Boolean>()

    fun filterMessage(filter: suspend MessageFilterScope<Config>.(Message) -> Boolean) { filters += filter }
    fun sideEffect(effect: suspend StateActionScope<Config>.() -> Unit) { sideEffects += effect }
    fun onMessage(handler: suspend StateActionScope<Config>.(Message) -> Boolean) { handlers += handler }
}

interface DialogStateScope<Config> {
    val config: Config
    val verticle: StatefulTelegramDialogVerticle<Config>
}

@StatefulDialogDsl
class MessageFilterScope<Config> internal constructor(
    override val config: Config,
    override val verticle: StatefulTelegramDialogVerticle<Config>
) : DialogStateScope<Config>

@StatefulDialogDsl
class StateActionScope<Config> internal constructor(
    override val config: Config,
    override val verticle: StatefulTelegramDialogVerticle<Config>,
    private val send: suspend (FormattedText, ReplyMarkup?, Boolean) -> Unit,
    private val finish: () -> Unit
) : DialogStateScope<Config> {
    internal var pendingTransition: PendingTransition<Config>? = null

    suspend fun sendOrEdit(
        text: FormattedText,
        replyMarkup: ReplyMarkup? = null,
        forceSend: Boolean = false
    ) = send(text, replyMarkup, forceSend)

    fun become(target: DialogStateTarget<Config>, historyBehavior: HistoryBehavior? = null) {
        pendingTransition = PendingTransition(target, historyBehavior)
    }

    fun complete() = finish()
}

internal data class PendingTransition<Config>(
    val target: DialogStateTarget<Config>,
    val historyBehavior: HistoryBehavior?
)

private class DslDialogVerticle<Config>(
    private val definition: DialogDefinition<Config>
) : StatefulTelegramDialogVerticle<Config>() {
    override fun resolveConfigJavaType(typeFactory: TypeFactory): JavaType =
        typeFactory.constructType(definition.configType)

    internal val configValue: Config get() = typedConfig

    override val chatId: Long get() = definition.chatIdProvider(configValue)
    override val initialState: State by lazy {
        instantiate(definition.initialProvider(DialogInitialScope(configValue)))
    }

    internal fun instantiate(target: DialogStateTarget<Config>): RuntimeState<Config> {
        require(target.definition.token === definition.token) { "state ${target.definition.displayName} belongs to another dialog" }
        require(target.definition.isDefined) { "state ${target.definition.displayName} is not defined" }
        return RuntimeState(this, target)
    }
}

private class RuntimeState<Config>(
    private val owner: DslDialogVerticle<Config>,
    private val target: DialogStateTarget<Config>
) : StatefulTelegramDialogVerticle.State(owner) {
    private val behavior = StateBuilder<Config>().apply(target.configure)

    override suspend fun shouldHandleMessage(message: Message): Boolean {
        val scope = MessageFilterScope(owner.configValue, owner)
        return behavior.filters.all { it(scope, message) }
    }

    override suspend fun sideEffect() {
        val scope = actionScope()
        behavior.sideEffects.forEach { it(scope) }
        commit(scope.pendingTransition)
    }

    override suspend fun handleMessage(message: Message) {
        val scope = actionScope()
        for (handler in behavior.handlers) {
            if (handler(scope, message)) break
        }
        commit(scope.pendingTransition)
    }

    private fun actionScope() = StateActionScope(
        config = owner.configValue,
        verticle = owner,
        send = { text, replyMarkup, forceSend -> sendOrEdit(text, replyMarkup, forceSend) },
        finish = { complete() }
    )

    private suspend fun commit(pending: PendingTransition<Config>?) {
        pending ?: return
        val next = owner.instantiate(pending.target)
        pending.historyBehavior?.let { become(next, it) } ?: become(next)
    }

    override fun toString(): String = buildString {
        append(target.definition.displayName)
        if (target.arguments.isNotEmpty()) target.arguments.joinTo(this, prefix = "(", postfix = ")")
    }
}
