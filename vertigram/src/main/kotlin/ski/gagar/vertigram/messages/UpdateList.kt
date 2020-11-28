package ski.gagar.vertigram.messages

import ski.gagar.vertigram.entities.Update

data class UpdateList(val updates: List<Update>) {
    constructor(upd: Update) : this(listOf(upd))
}