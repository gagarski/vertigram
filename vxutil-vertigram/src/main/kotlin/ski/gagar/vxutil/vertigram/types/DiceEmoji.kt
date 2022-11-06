package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonValue

enum class DiceEmoji(@JsonValue val emoji: String) {
    DICE("\uD83C\uDFB2"),
    DART("\uD83C\uDFAF"),
    BASKETBALL("\uD83C\uDFC0"),
    FOOTBALL("⚽️"),
    BOWLING("\uD83C\uDFB3"),
    SLOT("\uD83C\uDFB0");
}
