package replay.adaptadores.banco

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import replay.dominio.Pagina
import replay.dominio.RepositorioUsuario
import replay.dominio.Usuario

class RepositorioUsuarioExposed : RepositorioUsuario {

    private fun ResultRow.paraUsuario() = Usuario(
        id = this[UsuarioTabela.id],
        nome = this[UsuarioTabela.nome],
        email = this[UsuarioTabela.email],
        cidade = this[UsuarioTabela.cidade],
    )

    override suspend fun listar(cidade: String?, pagina: Int, tamanho: Int): Pagina<Usuario> = banco {
        var filtro: Op<Boolean> = Op.TRUE
        if (cidade != null) filtro = filtro and (UsuarioTabela.cidade.lowerCase() eq cidade.lowercase())

        val total = UsuarioTabela.selectAll().where(filtro).count()
        // Paginação no SQL (LIMIT/OFFSET), nada em memória.
        val itens = UsuarioTabela.selectAll().where(filtro)
            .orderBy(UsuarioTabela.id to SortOrder.ASC)
            .limit(tamanho)
            .offset(pagina.toLong() * tamanho)
            .map { it.paraUsuario() }
        Pagina(itens, pagina, tamanho, total)
    }

    override suspend fun buscar(id: Long): Usuario? = banco {
        UsuarioTabela.selectAll().where(UsuarioTabela.id eq id).singleOrNull()?.paraUsuario()
    }

    override suspend fun criar(usuario: Usuario): Usuario = banco {
        val novoId = UsuarioTabela.insert {
            it[UsuarioTabela.nome] = usuario.nome
            it[UsuarioTabela.email] = usuario.email
            it[UsuarioTabela.cidade] = usuario.cidade
        }[UsuarioTabela.id]
        usuario.copy(id = novoId)
    }

    override suspend fun atualizar(usuario: Usuario): Usuario? = banco {
        val id = requireNotNull(usuario.id)
        val linhas = UsuarioTabela.update({ UsuarioTabela.id eq id }) {
            it[UsuarioTabela.nome] = usuario.nome
            it[UsuarioTabela.email] = usuario.email
            it[UsuarioTabela.cidade] = usuario.cidade
        }
        if (linhas == 0) null else usuario
    }

    override suspend fun remover(id: Long): Boolean = banco {
        UsuarioTabela.deleteWhere { UsuarioTabela.id eq id } > 0
    }
}
