package replay.dominio

/** Portas: declaradas no domínio, implementadas em adaptadores/banco. */
interface RepositorioUsuario {
    suspend fun listar(cidade: String?, pagina: Int, tamanho: Int): Pagina<Usuario>
    suspend fun buscar(id: Long): Usuario?
    suspend fun criar(usuario: Usuario): Usuario
    suspend fun atualizar(usuario: Usuario): Usuario?
    suspend fun remover(id: Long): Boolean
}

interface RepositorioAnuncio {
    suspend fun listar(filtro: FiltroAnuncio, pagina: Int, tamanho: Int): Pagina<Anuncio>
    suspend fun buscar(id: Long): Anuncio?
    suspend fun criar(anuncio: Anuncio): Anuncio
    suspend fun atualizar(anuncio: Anuncio): Anuncio?
    suspend fun remover(id: Long): Boolean
}
