package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalTextWithEntities

@TelegramCodegen.Type
data class Game internal constructor(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    override val text: String? = null,
    val textEntities: List<MessageEntity>? = null,
    val animation: Animation? = null
) : HasOptionalTextWithEntities {
    @JsonIgnore
    override val entities = textEntities

    companion object
}
