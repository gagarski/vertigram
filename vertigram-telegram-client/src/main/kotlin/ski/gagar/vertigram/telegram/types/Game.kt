package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalTextWithEntities

data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    override val text: String? = null,
    val textEntities: List<MessageEntity>? = null,
    val animation: Animation? = null
) : HasOptionalTextWithEntities {
    @JsonIgnore
    override val entities = textEntities
}
