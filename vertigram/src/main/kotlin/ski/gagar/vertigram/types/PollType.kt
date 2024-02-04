package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class PollType{
    @JsonProperty("regular")
    REGULAR,
    @JsonProperty("quiz")
    QUIZ
}
