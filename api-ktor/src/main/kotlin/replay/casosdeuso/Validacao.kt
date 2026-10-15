package replay.casosdeuso

import replay.dominio.ErroValidacao

const val TAMANHO_MAXIMO = 100

fun validarPaginacao(pagina: Int, tamanho: Int) {
    val erros = mutableMapOf<String, String>()
    if (pagina < 0) erros["pagina"] = "deve ser maior ou igual a 0"
    if (tamanho !in 1..TAMANHO_MAXIMO) erros["tamanho"] = "deve estar entre 1 e $TAMANHO_MAXIMO"
    if (erros.isNotEmpty()) throw ErroValidacao(erros)
}

fun textoObrigatorio(erros: MutableMap<String, String>, campo: String, valor: String, max: Int) {
    when {
        valor.isBlank() -> erros[campo] = "é obrigatório"
        valor.trim().length > max -> erros[campo] = "deve ter no máximo $max caracteres"
    }
}
