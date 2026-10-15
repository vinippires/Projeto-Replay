package replay.adaptadores.http

import java.math.BigDecimal
import kotlinx.serialization.Serializable
import replay.dominio.*

@Serializable
data class UsuarioEntrada(val nome: String, val email: String, val cidade: String)

@Serializable
data class UsuarioSaida(val id: Long, val nome: String, val email: String, val cidade: String)

@Serializable
data class AnuncioEntrada(
    val titulo: String,
    val artista: String,
    val formato: String,
    val estado: String,
    val preco: Double,
    val status: String? = null,
    /** Só é usado em POST /anuncios; em /usuarios/{id}/anuncios o dono vem da URL. */
    val usuarioId: Long? = null,
)

@Serializable
data class AnuncioSaida(
    val id: Long,
    val usuarioId: Long,
    val titulo: String,
    val artista: String,
    val formato: String,
    val estado: String,
    val preco: Double,
    val status: String,
)

@Serializable
data class PaginaDto<T>(val itens: List<T>, val pagina: Int, val tamanho: Int, val total: Long)

/** Corpo de erro no formato RFC 9457 (application/problem+json). */
@Serializable
data class Problema(
    val type: String = "about:blank",
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String? = null,
    val erros: Map<String, String>? = null,
)

// ---------- conversões (num lugar só) ----------

fun Usuario.paraSaida() = UsuarioSaida(requireNotNull(id), nome, email, cidade)
fun UsuarioEntrada.paraDominio() = Usuario(nome = nome, email = email, cidade = cidade)

fun Anuncio.paraSaida() = AnuncioSaida(
    id = requireNotNull(id), usuarioId = usuarioId, titulo = titulo, artista = artista,
    formato = formato.name, estado = estado.name, preco = preco.toDouble(), status = status.name,
)

fun AnuncioEntrada.paraDados() = DadosAnuncio(
    titulo = titulo,
    artista = artista,
    formato = enumDe<Formato>("formato", formato),
    estado = enumDe<Estado>("estado", estado),
    preco = BigDecimal.valueOf(preco),
    status = status?.let { enumDe<StatusAnuncio>("status", it) } ?: StatusAnuncio.DISPONIVEL,
)

fun <T, R> Pagina<T>.mapear(f: (T) -> R) = PaginaDto(itens.map(f), pagina, tamanho, total)

inline fun <reified E : Enum<E>> enumDe(campo: String, valor: String): E =
    enumValues<E>().firstOrNull { it.name.equals(valor.trim(), ignoreCase = true) }
        ?: throw ErroValidacao(mapOf(campo to "valor inválido; use: ${enumValues<E>().joinToString()}"))
