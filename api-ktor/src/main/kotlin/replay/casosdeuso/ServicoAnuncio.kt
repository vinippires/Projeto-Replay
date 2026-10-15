package replay.casosdeuso

import java.math.BigDecimal
import java.math.RoundingMode
import replay.dominio.*

class ServicoAnuncio(
    private val anuncios: RepositorioAnuncio,
    private val usuarios: RepositorioUsuario,
) {
    suspend fun listar(filtro: FiltroAnuncio, pagina: Int, tamanho: Int): Pagina<Anuncio> {
        validarPaginacao(pagina, tamanho)
        return anuncios.listar(
            filtro.copy(titulo = filtro.titulo?.trim()?.ifBlank { null }),
            pagina,
            tamanho,
        )
    }

    suspend fun buscar(id: Long): Anuncio =
        anuncios.buscar(id) ?: throw NaoEncontrado("Anúncio $id não encontrado")

    suspend fun criar(usuarioId: Long, dados: DadosAnuncio): Anuncio {
        if (usuarios.buscar(usuarioId) == null) throw NaoEncontrado("Usuário $usuarioId não encontrado")
        val d = normalizar(dados)
        return anuncios.criar(
            Anuncio(
                usuarioId = usuarioId, titulo = d.titulo, artista = d.artista,
                formato = d.formato, estado = d.estado, preco = d.preco, status = d.status,
            ),
        )
    }

    suspend fun atualizar(id: Long, dados: DadosAnuncio): Anuncio {
        val atual = buscar(id)
        val d = normalizar(dados)
        return anuncios.atualizar(
            atual.copy(
                titulo = d.titulo, artista = d.artista, formato = d.formato,
                estado = d.estado, preco = d.preco, status = d.status,
            ),
        ) ?: throw NaoEncontrado("Anúncio $id não encontrado")
    }

    suspend fun remover(id: Long) {
        if (!anuncios.remover(id)) throw NaoEncontrado("Anúncio $id não encontrado")
    }

    private fun normalizar(d: DadosAnuncio): DadosAnuncio {
        val erros = mutableMapOf<String, String>()
        textoObrigatorio(erros, "titulo", d.titulo, 200)
        textoObrigatorio(erros, "artista", d.artista, 200)
        if (d.preco < BigDecimal.ZERO) erros["preco"] = "não pode ser negativo"
        else if (d.preco > BigDecimal("99999999.99")) erros["preco"] = "valor muito alto"
        if (erros.isNotEmpty()) throw ErroValidacao(erros)
        return d.copy(
            titulo = d.titulo.trim(),
            artista = d.artista.trim(),
            preco = d.preco.setScale(2, RoundingMode.HALF_UP),
        )
    }
}
