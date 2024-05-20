package com.fiap.postech.configuration

import com.fiap.postech.infrastructure.persistence.entities.Orders
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.util.logging.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DatabaseSingleton {
    fun init(config: ApplicationConfig, logger: Logger) {

        val hikariConfig = HikariConfig().apply {
//            jdbcUrl = config.property("storage.url").getString()
//            driverClassName = config.property("storage.driver").getString()
//            username = config.property("storage.user").getString()
//            password = config.property("storage.password").getString()
//            maximumPoolSize = config.property("storage.poolSize").getString().toInt()
            jdbcUrl = "jdbc:postgresql://localhost:5432/orders-db"
            driverClassName = "org.postgresql.Driver"
            username = "food-challenge"
            password = "root"
            maximumPoolSize = 10
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        try {
            transaction {
                ordersMigration()
            }
            logger.info("Database connection and migration successful.")
        } catch (ex: Exception) {
            logger.error("Error during database connection or migration: ${ex.message}", ex)
            throw ex
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T? =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

private fun ordersMigration() {
    SchemaUtils.create(Orders)
}
