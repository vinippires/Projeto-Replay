@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)

package replay.adaptadores.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import replay.dominio.Conflito
import replay.dominio.ErroValidacao
import replay.dominio.NaoEncontrado

val ProblemJson = ContentType("application", "problem+json")
private val json = Json { explicitNulls = false; encodeDefaults = true }

private suspend fun ApplicationCall.problema(status: HttpStatusCode, title: String, detail: String, erros: Map<String, String>? = null) {
    val corpo = Problema(
        title = title, status = status.value, detail = detail,
        instance = request.local.uri, erros = erros,
    )
    respondText(json.encodeToString(corpo), ProblemJson, status)
}

/** Único lugar que transforma exceções em respostas HTTP. */
fun Application.configurarErros() {
    install(StatusPages) {
        exception<ErroValidacao> { call, e ->
            call.problema(HttpStatusCode.BadRequest, "Dados inválidos", "Um ou mais campos são inválidos.", e.erros)
        }
        exception<NaoEncontrado> { call, e ->
            call.problema(HttpStatusCode.NotFound, "Não encontrado", e.message ?: "Recurso não encontrado.")
        }
        exception<Conflito> { call, e ->
            call.problema(HttpStatusCode.Conflict, "Conflito", e.message ?: "Conflito com o estado atual.")
        }
        // JSON malformado, campo obrigatório ausente ou tipo errado no corpo
        exception<ContentTransformationException> { call, _ ->
            call.problema(HttpStatusCode.BadRequest, "Corpo inválido", "O corpo da requisição não é um JSON válido ou está incompleto.")
        }
        exception<BadRequestException> { call, _ ->
            call.problema(HttpStatusCode.BadRequest, "Requisição inválida", "A requisição está mal formada.")
        }
        exception<Throwable> { call, e ->
            call.application.environment.log.error("Erro inesperado", e)
            call.problema(HttpStatusCode.InternalServerError, "Erro interno", "Ocorreu um erro inesperado.")
        }
    }
}
