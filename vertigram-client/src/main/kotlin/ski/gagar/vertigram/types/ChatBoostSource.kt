package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "source")
@JsonSubTypes(
    JsonSubTypes.Type(value = ChatBoostSourcePremium::class, name = ChatBoostSourceType.PREMIUM_STR),
    JsonSubTypes.Type(value = ChatBoostSourceGiftCode::class, name = ChatBoostSourceType.GIFT_CODE_STR),
    JsonSubTypes.Type(value = ChatBoostSourceGiveaway::class, name = ChatBoostSourceType.GIVEAWAY_STR)
)
sealed interface ChatBoostSource {
    val source: ChatBoostSourceType
}

