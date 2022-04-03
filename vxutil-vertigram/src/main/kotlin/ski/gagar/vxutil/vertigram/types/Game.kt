package ski.gagar.vxutil.vertigram.types

data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String? = null,
    val textEntities: List<MessageEntity>? = null,
    val animation: Animation? = null
)

val Game.textEntitiesInstantiated: List<InstantiatedEntity>
    get() = textEntities?.map { InstantiatedEntity(it, this.text) } ?: listOf()
