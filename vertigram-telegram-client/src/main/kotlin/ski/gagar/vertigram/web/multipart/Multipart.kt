package ski.gagar.vertigram.web.multipart

import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.HttpRequest
suspend fun HttpRequest<Buffer>.sendMultipartForm(form: MultipartForm) = form.send(this)
