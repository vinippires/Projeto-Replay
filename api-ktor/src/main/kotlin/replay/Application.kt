package replay

import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import replay.adaptadores.banco.*
import replay.adaptadores.http.configurarErros
import replay.adaptadores.http.rotasAnuncios
import replay.adaptadores.http.rotasUsuarios
import replay.casosdeuso.ServicoAnuncio
import replay.casosdeuso.ServicoUsuario

fun main() {
    val porta = (System.getenv("PORT") ?: "8080").toInt()
    embeddedServer(Netty, port = porta, host = "0.0.0.0", module = Application::modulo).start(wait = true)
}

fun Application.modulo() {
    // Banco: pool -> migrações Flyway -> Exposed
    val ds = criarDataSource()
    migrar(ds)
    conectar(ds)

    // Composição das camadas (única classe que conhece as implementações concretas)
    val repoUsuario = RepositorioUsuarioExposed()
    val repoAnuncio = RepositorioAnuncioExposed()
    val servicoUsuario = ServicoUsuario(repoUsuario)
    val servicoAnuncio = ServicoAnuncio(repoAnuncio, repoUsuario)

    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true; prettyPrint = true })
    }
    install(OpenApi) {
        info {
            title = "Replay API"
            version = "1.0.0"
            description = "API de anúncios de CDs e DVDs usados (Sprint 1: CRUD de Usuários e Anúncios)."
        }
    }
    configurarErros()

    routing {
        get("/") { call.respondRedirect("/swagger") }
        route("/api.json") { openApi() }       // especificação OpenAPI gerada do código
        route("/swagger") { swaggerUI("/api.json") } // Swagger UI
        rotasUsuarios(servicoUsuario, servicoAnuncio)
        rotasAnuncios(servicoAnuncio)
    }
}
