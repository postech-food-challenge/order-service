package br.com.fiap.postech.configuration

import br.com.fiap.postech.infrastructure.persistence.entities.Orders
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import io.ktor.util.logging.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    private const val MAX_RETRIES = 10
    private const val RETRY_DELAY_MS = 1000L

    fun init(config: ApplicationConfig, logger: Logger) {

        val host = config.property("storage.host").getString()
        val port = config.property("storage.port").getString()
        val database = config.property("storage.database").getString()
        val user = config.property("storage.user").getString()
        val password = config.property("storage.password").getString()
        val poolSize = config.property("storage.poolSize").getString().toInt()

        val jdbcUrl = "jdbc:postgresql://$host:$port/$database"

        val hikariConfig = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.driverClassName = config.property("storage.driver").getString()
            this.username = user
            this.password = password
            this.maximumPoolSize = poolSize
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        var attempts = 0
        var connected = false

        while (!connected) {
            try {
                val dataSource = HikariDataSource(hikariConfig)
                Database.connect(dataSource)
                transaction {
                    ordersMigration()
                }
                logger.info("Database connection and migration successful.")
                connected = true
            } catch (ex: Exception) {
                attempts++
                logger.error(
                    "Error during database connection or migration: ${ex.message}. Attempt $attempts of $MAX_RETRIES",
                    ex
                )
                if (attempts < MAX_RETRIES) {
                    Thread.sleep(RETRY_DELAY_MS)
                } else {
                    throw ex
                }
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T? =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

private fun ordersMigration() {
    SchemaUtils.create(Orders)
}
