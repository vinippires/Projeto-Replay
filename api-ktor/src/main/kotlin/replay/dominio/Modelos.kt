package replay.dominio

import java.math.BigDecimal

enum class Formato { CD, DVD }
enum class Estado { NOVO, SEMINOVO, USADO, DESGASTADO }
enum class StatusAnuncio { DISPONIVEL, NEGOCIADO }

data class Usuario(
    val id: Long? = null,
    val nome: String,
    val email: String,
    val cidade: String,
)

/** Dados editáveis de um anúncio (o dono e o id não mudam). */
data class DadosAnuncio(
    val titulo: String,
    val artista: String,
    val formato: Formato,
    val estado: Estado,
    val preco: BigDecimal,
    val status: StatusAnuncio = StatusAnuncio.DISPONIVEL,
)

data class Anuncio(
    val id: Long? = null,
    val usuarioId: Long,
    val titulo: String,
    val artista: String,
    val formato: Formato,
    val estado: Estado,
    val preco: BigDecimal,
    val status: StatusAnuncio = StatusAnuncio.DISPONIVEL,
)

data class FiltroAnuncio(
    val formato: Formato? = null,
    val status: StatusAnuncio? = null,
    val titulo: String? = null,
    val usuarioId: Long? = null,
)

data class Pagina<T>(
    val itens: List<T>,
    val pagina: Int,
    val tamanho: Int,
    val total: Long,
)
