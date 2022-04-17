package ski.gagar.vertigram.annotations

@Retention(AnnotationRetention.SOURCE)
annotation class TgMethod(val type: String = JSON) {
    companion object {
        const val JSON = "json"
        const val MULTIPART = "multipart"
    }
}
