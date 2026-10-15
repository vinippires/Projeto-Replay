package replay.adaptadores.banco

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import replay.dominio.*

class RepositorioAnuncioExposed : RepositorioAnuncio {

    private fun ResultRow.paraAnuncio() = Anuncio(
        id = this[AnuncioTabela.id],
        usuarioId = this[AnuncioTabela.usuarioId],
        titulo = this[AnuncioTabela.titulo],
        artista = this[AnuncioTabela.artista],
        formato = Formato.valueOf(this[AnuncioTabela.formato]),
        estado = Estado.valueOf(this[AnuncioTabela.estado]),
        preco = this[AnuncioTabela.preco],
        status = StatusAnuncio.valueOf(this[AnuncioTabela.status]),
    )

    override suspend fun listar(filtro: FiltroAnuncio, pagina: Int, tamanho: Int): Pagina<Anuncio> = banco {
        var cond: Op<Boolean> = Op.TRUE
        filtro.formato?.let { cond = cond and (AnuncioTabela.formato eq it.name) }
        filtro.status?.let { cond = cond and (AnuncioTabela.status eq it.name) }
        filtro.usuarioId?.let { cond = cond and (AnuncioTabela.usuarioId eq it) }
        filtro.titulo?.let {
            val termo = it.lowercase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_")
            cond = cond and (AnuncioTabela.titulo.lowerCase() like "%$termo%")
        }

        val total = AnuncioTabela.selectAll().where(cond).count()
        val itens = AnuncioTabela.selectAll().where(cond)
            .orderBy(AnuncioTabela.id to SortOrder.ASC)
            .limit(tamanho)
            .offset(pagina.toLong() * tamanho)
            .map { it.paraAnuncio() }
        Pagina(itens, pagina, tamanho, total)
    }

    override suspend fun buscar(id: Long): Anuncio? = banco {
        AnuncioTabela.selectAll().where(AnuncioTabela.id eq id).singleOrNull()?.paraAnuncio()
    }

    override suspend fun criar(anuncio: Anuncio): Anuncio = banco {
        val novoId = AnuncioTabela.insert {
            it[AnuncioTabela.usuarioId] = anuncio.usuarioId
            it[AnuncioTabela.titulo] = anuncio.titulo
            it[AnuncioTabela.artista] = anuncio.artista
            it[AnuncioTabela.formato] = anuncio.formato.name
            it[AnuncioTabela.estado] = anuncio.estado.name
            it[AnuncioTabela.preco] = anuncio.preco
            it[AnuncioTabela.status] = anuncio.status.name
        }[AnuncioTabela.id]
        anuncio.copy(id = novoId)
    }

    override suspend fun atualizar(anuncio: Anuncio): Anuncio? = banco {
        val id = requireNotNull(anuncio.id)
        val linhas = AnuncioTabela.update({ AnuncioTabela.id eq id }) {
            it[AnuncioTabela.titulo] = anuncio.titulo
            it[AnuncioTabela.artista] = anuncio.artista
            it[AnuncioTabela.formato] = anuncio.formato.name
            it[AnuncioTabela.estado] = anuncio.estado.name
            it[AnuncioTabela.preco] = anuncio.preco
            it[AnuncioTabela.status] = anuncio.status.name
        }
        if (linhas == 0) null else anuncio
    }

    override suspend fun remover(id: Long): Boolean = banco {
        AnuncioTabela.deleteWhere { AnuncioTabela.id eq id } > 0
    }
}
