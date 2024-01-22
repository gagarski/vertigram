package ski.gagar.vxutil.vertigram.types

data class LinkPreviewOptions(
    @get:JvmName("getIsDisabled")
    val isDisabled: Boolean = false,
    val url: String? = null,
    val preferSmallMedia: Boolean = false,
    val preferLargeMedia: Boolean = false,
    val showAboveText: Boolean = false
)
