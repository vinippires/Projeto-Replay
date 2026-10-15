package replay.adaptadores.http

import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.github.smiley4.ktoropenapi.put
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import replay.casosdeuso.ServicoAnuncio
import replay.casosdeuso.ServicoUsuario
import replay.dominio.*

// ---------- helpers de parâmetros ----------

private fun ApplicationCall.idDaRota(nome: String = "id"): Long =
    parameters[nome]?.toLongOrNull()
        ?: throw ErroValidacao(mapOf(nome to "deve ser um número inteiro"))

private fun ApplicationCall.inteiro(nome: String, padrao: Int): Int {
    val bruto = request.queryParameters[nome] ?: return padrao
    return bruto.toIntOrNull() ?: throw ErroValidacao(mapOf(nome to "deve ser um número inteiro"))
}

private fun ApplicationCall.pagina() = inteiro("pagina", 0)
private fun ApplicationCall.tamanho() = inteiro("tamanho", 20)

private fun ApplicationCall.filtroAnuncio(usuarioId: Long? = null): FiltroAnuncio = FiltroAnuncio(
    formato = request.queryParameters["formato"]?.let { enumDe<Formato>("formato", it) },
    status = request.queryParameters["status"]?.let { enumDe<StatusAnuncio>("status", it) },
    titulo = request.queryParameters["titulo"],
    usuarioId = usuarioId ?: request.queryParameters["usuarioId"]?.let {
        it.toLongOrNull() ?: throw ErroValidacao(mapOf("usuarioId" to "deve ser um número inteiro"))
    },
)

// ---------- rotas ----------

fun Route.rotasUsuarios(usuarios: ServicoUsuario, anuncios: ServicoAnuncio) {

    get("/usuarios", {
        tags("Usuários")
        summary = "Lista usuários (paginado)"
        request {
            queryParameter<Int>("pagina") { description = "Página, começando em 0"; required = false }
            queryParameter<Int>("tamanho") { description = "Itens por página (1 a 100, padrão 20)"; required = false }
            queryParameter<String>("cidade") { description = "Filtra por cidade"; required = false }
        }
        response {
            code(HttpStatusCode.OK) { description = "Página de usuários"; body<PaginaDto<UsuarioSaida>>() }
            code(HttpStatusCode.BadRequest) { description = "Parâmetros inválidos"; body<Problema>() }
        }
    }) {
        val r = usuarios.listar(call.request.queryParameters["cidade"], call.pagina(), call.tamanho())
        call.respond(r.mapear { it.paraSaida() })
    }

    get("/usuarios/{id}", {
        tags("Usuários")
        summary = "Busca um usuário por id"
        request { pathParameter<Long>("id") { description = "Id do usuário" } }
        response {
            code(HttpStatusCode.OK) { body<UsuarioSaida>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.NotFound) { body<Problema>() }
        }
    }) {
        call.respond(usuarios.buscar(call.idDaRota()).paraSaida())
    }

    post("/usuarios", {
        tags("Usuários")
        summary = "Cria um usuário"
        request { body<UsuarioEntrada>() }
        response {
            code(HttpStatusCode.Created) { description = "Criado; cabeçalho Location aponta para o recurso"; body<UsuarioSaida>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.Conflict) { description = "E-mail já cadastrado"; body<Problema>() }
        }
    }) {
        val criado = usuarios.criar(call.receive<UsuarioEntrada>().paraDominio())
        call.response.header(HttpHeaders.Location, "/usuarios/${criado.id}")
        call.respond(HttpStatusCode.Created, criado.paraSaida())
    }

    put("/usuarios/{id}", {
        tags("Usuários")
        summary = "Atualiza um usuário"
        request { pathParameter<Long>("id"); body<UsuarioEntrada>() }
        response {
            code(HttpStatusCode.OK) { body<UsuarioSaida>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.NotFound) { body<Problema>() }
            code(HttpStatusCode.Conflict) { body<Problema>() }
        }
    }) {
        val atualizado = usuarios.atualizar(call.idDaRota(), call.receive<UsuarioEntrada>().paraDominio())
        call.respond(atualizado.paraSaida())
    }

    delete("/usuarios/{id}", {
        tags("Usuários")
        summary = "Remove um usuário"
        request { pathParameter<Long>("id") }
        response {
            code(HttpStatusCode.NoContent) { description = "Removido" }
            code(HttpStatusCode.NotFound) { body<Problema>() }
            code(HttpStatusCode.Conflict) { description = "Usuário ainda possui anúncios"; body<Problema>() }
        }
    }) {
        usuarios.remover(call.idDaRota())
        call.respond(HttpStatusCode.NoContent)
    }

    // ----- relacionamento 1:N: anúncios de um usuário -----

    get("/usuarios/{id}/anuncios", {
        tags("Usuários")
        summary = "Lista os anúncios de um usuário (paginado, com filtros)"
        request {
            pathParameter<Long>("id")
            queryParameter<Int>("pagina") { required = false }
            queryParameter<Int>("tamanho") { required = false }
            queryParameter<String>("formato") { description = "CD ou DVD"; required = false }
            queryParameter<String>("status") { description = "DISPONIVEL ou NEGOCIADO"; required = false }
            queryParameter<String>("titulo") { description = "Busca parcial no título"; required = false }
        }
        response {
            code(HttpStatusCode.OK) { body<PaginaDto<AnuncioSaida>>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.NotFound) { body<Problema>() }
        }
    }) {
        val id = call.idDaRota()
        usuarios.buscar(id) // 404 se o usuário não existir
        val r = anuncios.listar(call.filtroAnuncio(usuarioId = id), call.pagina(), call.tamanho())
        call.respond(r.mapear { it.paraSaida() })
    }

    post("/usuarios/{id}/anuncios", {
        tags("Usuários")
        summary = "Cria um anúncio para o usuário"
        request { pathParameter<Long>("id"); body<AnuncioEntrada>() }
        response {
            code(HttpStatusCode.Created) { body<AnuncioSaida>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.NotFound) { description = "Usuário não existe"; body<Problema>() }
        }
    }) {
        val dono = call.idDaRota()
        val criado = anuncios.criar(dono, call.receive<AnuncioEntrada>().paraDados())
        call.response.header(HttpHeaders.Location, "/anuncios/${criado.id}")
        call.respond(HttpStatusCode.Created, criado.paraSaida())
    }
}

fun Route.rotasAnuncios(anuncios: ServicoAnuncio) {

    get("/anuncios", {
        tags("Anúncios")
        summary = "Lista anúncios (paginado, com filtros)"
        request {
            queryParameter<Int>("pagina") { description = "Página, começando em 0"; required = false }
            queryParameter<Int>("tamanho") { description = "Itens por página (1 a 100, padrão 20)"; required = false }
            queryParameter<String>("formato") { description = "CD ou DVD"; required = false }
            queryParameter<String>("status") { description = "DISPONIVEL ou NEGOCIADO"; required = false }
            queryParameter<String>("titulo") { description = "Busca parcial no título"; required = false }
            queryParameter<Long>("usuarioId") { description = "Filtra pelo dono"; required = false }
        }
        response {
            code(HttpStatusCode.OK) { body<PaginaDto<AnuncioSaida>>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
        }
    }) {
        val r = anuncios.listar(call.filtroAnuncio(), call.pagina(), call.tamanho())
        call.respond(r.mapear { it.paraSaida() })
    }

    get("/anuncios/{id}", {
        tags("Anúncios")
        summary = "Busca um anúncio por id"
        request { pathParameter<Long>("id") }
        response {
            code(HttpStatusCode.OK) { body<AnuncioSaida>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.NotFound) { body<Problema>() }
        }
    }) {
        call.respond(anuncios.buscar(call.idDaRota()).paraSaida())
    }

    post("/anuncios", {
        tags("Anúncios")
        summary = "Cria um anúncio (informe usuarioId no corpo)"
        request { body<AnuncioEntrada>() }
        response {
            code(HttpStatusCode.Created) { body<AnuncioSaida>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.NotFound) { description = "Usuário não existe"; body<Problema>() }
        }
    }) {
        val entrada = call.receive<AnuncioEntrada>()
        val dono = entrada.usuarioId ?: throw ErroValidacao(mapOf("usuarioId" to "é obrigatório"))
        val criado = anuncios.criar(dono, entrada.paraDados())
        call.response.header(HttpHeaders.Location, "/anuncios/${criado.id}")
        call.respond(HttpStatusCode.Created, criado.paraSaida())
    }

    put("/anuncios/{id}", {
        tags("Anúncios")
        summary = "Atualiza um anúncio (inclui mudar o status para NEGOCIADO)"
        request { pathParameter<Long>("id"); body<AnuncioEntrada>() }
        response {
            code(HttpStatusCode.OK) { body<AnuncioSaida>() }
            code(HttpStatusCode.BadRequest) { body<Problema>() }
            code(HttpStatusCode.NotFound) { body<Problema>() }
        }
    }) {
        val atualizado = anuncios.atualizar(call.idDaRota(), call.receive<AnuncioEntrada>().paraDados())
        call.respond(atualizado.paraSaida())
    }

    delete("/anuncios/{id}", {
        tags("Anúncios")
        summary = "Remove um anúncio"
        request { pathParameter<Long>("id") }
        response {
            code(HttpStatusCode.NoContent) { description = "Removido" }
            code(HttpStatusCode.NotFound) { body<Problema>() }
        }
    }) {
        anuncios.remover(call.idDaRota())
        call.respond(HttpStatusCode.NoContent)
    }
}
