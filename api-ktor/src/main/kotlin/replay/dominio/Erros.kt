package replay.dominio

/** Erros de negócio. A camada HTTP os traduz em status + problem details. */
sealed class ErroDominio(mensagem: String) : RuntimeException(mensagem)

class ErroValidacao(val erros: Map<String, String>) : ErroDominio("Dados inválidos")
class NaoEncontrado(mensagem: String) : ErroDominio(mensagem)
class Conflito(mensagem: String) : ErroDominio(mensagem)
