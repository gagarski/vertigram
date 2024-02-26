package ski.gagar.vertigram.verticles.common.address

object VertigramCommonAddress {
    object Private {
        fun withClassifier(id: String, classifier: String) =
            "ski.gagar.vertigram.private.$id.$classifier"
    }

    object PostOffice {
        object Classifier {
            const val Subscribe = "subscribe"
            const val Unsubscribe = "unsubscribe"
        }
    }
}
