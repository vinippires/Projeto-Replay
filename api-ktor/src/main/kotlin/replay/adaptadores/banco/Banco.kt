package replay.adaptadores.banco

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replay.dominio.Conflito
import replay.dominio.ErroValidacao

private fun env(nome: String, padrao: String) = System.getenv(nome) ?: padrao

/** URL, usuário e senha vêm de variáveis de ambiente (com padrões para desenvolvimento). */
fun criarDataSource(): HikariDataSource {
    val config = HikariConfig().apply {
        jdbcUrl = env("DB_URL", "jdbc:postgresql://localhost:5432/replay")
        username = env("DB_USER", "replay")
        password = env("DB_PASSWORD", "replay")
        maximumPoolSize = 10
    }
    return HikariDataSource(config)
}

/** O esquema vem SOMENTE das migrações Flyway (nada de SchemaUtils.create). */
fun migrar(ds: HikariDataSource) {
    Flyway.configure()
        .dataSource(ds)
        .locations("classpath:db/migration")
        .load()
        .migrate()
}

fun conectar(ds: HikariDataSource) {
    Database.connect(ds)
}

/**
 * Toda operação de banco passa por aqui: transação suspensa + tradução das
 * violações de restrição do PostgreSQL em erros de domínio (nunca viram 500).
 */
suspend fun <T> banco(bloco: suspend Transaction.() -> T): T =
    try {
        newSuspendedTransaction(Dispatchers.IO, statement = bloco)
    } catch (e: ExposedSQLException) {
        when (e.sqlState) {
            "23505" -> throw Conflito("Já existe um registro com esse valor único (ex.: e-mail já cadastrado)")
            "23503" -> throw Conflito("Operação viola um relacionamento (registro referenciado ou referência inexistente)")
            "23502", "23514" -> throw ErroValidacao(mapOf("banco" to "valor viola uma restrição do banco"))
            else -> throw e
        }
    }
