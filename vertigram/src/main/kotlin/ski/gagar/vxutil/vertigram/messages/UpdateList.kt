package ski.gagar.vxutil.vertigram.messages

import ski.gagar.vxutil.vertigram.entities.Update

data class UpdateList(val updates: List<Update>) {
    constructor(upd: Update) : this(listOf(upd))
}
