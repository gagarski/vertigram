package ski.gagar.vertigram.samples

import com.fasterxml.jackson.core.type.TypeReference
import ski.gagar.vertigram.util.jackson.typeReference
import ski.gagar.vertigram.verticles.common.VertigramVerticle

private fun typeReferenceExample() {
    data class Config(val something: String)

    class ExampleVerticle : VertigramVerticle<Config>() {
        override val configTypeReference: TypeReference<Config> = typeReference()
    }
}