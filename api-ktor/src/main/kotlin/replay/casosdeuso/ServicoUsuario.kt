package replay.casosdeuso

import replay.dominio.*

class ServicoUsuario(private val repo: RepositorioUsuario) {

    private val regexEmail = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    suspend fun listar(cidade: String?, pagina: Int, tamanho: Int): Pagina<Usuario> {
        validarPaginacao(pagina, tamanho)
        return repo.listar(cidade?.trim()?.ifBlank { null }, pagina, tamanho)
    }

    suspend fun buscar(id: Long): Usuario =
        repo.buscar(id) ?: throw NaoEncontrado("Usuário $id não encontrado")

    suspend fun criar(entrada: Usuario): Usuario =
        repo.criar(normalizar(entrada))

    suspend fun atualizar(id: Long, entrada: Usuario): Usuario {
        buscar(id)
        return repo.atualizar(normalizar(entrada).copy(id = id))
            ?: throw NaoEncontrado("Usuário $id não encontrado")
    }

    suspend fun remover(id: Long) {
        if (!repo.remover(id)) throw NaoEncontrado("Usuário $id não encontrado")
    }

    private fun normalizar(u: Usuario): Usuario {
        val erros = mutableMapOf<String, String>()
        textoObrigatorio(erros, "nome", u.nome, 120)
        textoObrigatorio(erros, "cidade", u.cidade, 100)
        if (u.email.isBlank()) erros["email"] = "é obrigatório"
        else if (!regexEmail.matches(u.email.trim())) erros["email"] = "formato inválido"
        else if (u.email.trim().length > 200) erros["email"] = "deve ter no máximo 200 caracteres"
        if (erros.isNotEmpty()) throw ErroValidacao(erros)
        return u.copy(nome = u.nome.trim(), email = u.email.trim().lowercase(), cidade = u.cidade.trim())
    }
}
