package replay.adaptadores.banco

import org.jetbrains.exposed.sql.Table

// Só mapeamento. O esquema é criado pelas migrações Flyway.
object UsuarioTabela : Table("usuario") {
    val id = long("id").autoIncrement()
    val nome = varchar("nome", 120)
    val email = varchar("email", 200)
    val cidade = varchar("cidade", 100)
    override val primaryKey = PrimaryKey(id)
}

object AnuncioTabela : Table("anuncio") {
    val id = long("id").autoIncrement()
    val usuarioId = long("usuario_id")
    val titulo = varchar("titulo", 200)
    val artista = varchar("artista", 200)
    val formato = varchar("formato", 10)
    val estado = varchar("estado", 20)
    val preco = decimal("preco", 10, 2)
    val status = varchar("status", 15)
    override val primaryKey = PrimaryKey(id)
}
