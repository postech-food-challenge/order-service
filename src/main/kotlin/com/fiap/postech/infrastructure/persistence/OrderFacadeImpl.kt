package com.fiap.postech.infrastructure.persistence

import com.fiap.postech.configuration.DatabaseSingleton.dbQuery
import com.fiap.postech.domain.exceptions.DatabaseOperationException
import com.fiap.postech.infrastructure.persistence.entities.OrderEntity
import com.fiap.postech.infrastructure.persistence.entities.Orders
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class OrderFacadeImpl : OrderFacade {

    private fun resultRowToOrderEntity(row: ResultRow?): OrderEntity? {
        return if (row == null) {
            null
        } else {
            OrderEntity(
                id = row[Orders.orderId],
                customerCpf = row[Orders.customerCpf],
                status = row[Orders.status],
                createdAt = row[Orders.createdAt],
                paymentValidated = row[Orders.paymentValidated],
                price = row[Orders.price],
                qrData = row[Orders.qrData]
            )
        }
    }

    override suspend fun insert(order: OrderEntity): OrderEntity = dbQuery {
        Orders.insert {
            it[orderId] = order.id
            it[customerCpf] = order.customerCpf
            it[status] = order.status
            it[createdAt] = order.createdAt
            it[paymentValidated] = order.paymentValidated
            it[price] = order.price
            it[qrData] = order.qrData
        }

        order
    } ?: throw DatabaseOperationException("Failed to insert order")

    override suspend fun findById(id: String): OrderEntity? = dbQuery {
        Orders
            .select { Orders.orderId eq id }
            .map(::resultRowToOrderEntity)
            .singleOrNull()
    }
}