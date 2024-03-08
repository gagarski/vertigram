package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.newInstance
import javax.inject.Inject

abstract class VertigramJooqExtension @Inject constructor(val objectFactory: ObjectFactory, val project: Project) {
    abstract val generator: Property<String>
    abstract val jooqDatabase: Property<String>
    abstract val schema: Property<String>
    abstract val driver: Property<String>
    abstract val packageName: Property<String>
    abstract val directory: Property<String>
    @get:Optional
    abstract val testContainer: Property<TestContainer>
    @get:Optional
    abstract val liveDb: Property<LiveDb>
    abstract val config: Property<Config>

    fun configuration(action: Action<Config>) {
        val conf = if (config.isPresent) config.get() else objectFactory.newInstance<Config>()
        action.execute(conf)
        config.set(conf)
        conf.propagateTo(project)
    }
}