package com.fiap.postech.infrastructure.persistence.entities

import com.fiap.postech.domain.entities.Order
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Orders : LongIdTable("orders") {
    val orderId = varchar("order_id", 36)
    val customerCpf = varchar("customer_cpf", 11).nullable()
    val status = varchar("status", 255)
    val createdAt = datetime("created_at")
    val paymentValidated = bool("payment_validated").nullable()
    val price = integer("price").nullable()
    val qrData = varchar("qr_data", 255).nullable()

    fun fromDomain(domainObject: Order): OrderEntity {
        return OrderEntity(
            id = domainObject.id,
            customerCpf = domainObject.customerCpf?.value,
            status = domainObject.status.name,
            createdAt = domainObject.createdAt,
            paymentValidated = domainObject.paymentValidated,
            price = domainObject.price,
            qrData = domainObject.qrData
        )
    }
}

data class OrderEntity(
    val id: String,
    val customerCpf: String?,
    val status: String,
    val createdAt: LocalDateTime,
    val paymentValidated: Boolean?,
    val price: Int?,
    val qrData: String?
) {
    companion object {
        fun fromDomain(domainObject: Order): OrderEntity {
            return OrderEntity(
                id = domainObject.id,
                customerCpf = domainObject.customerCpf?.value,
                status = domainObject.status.name,
                createdAt = domainObject.createdAt,
                paymentValidated = domainObject.paymentValidated,
                price = domainObject.price,
                qrData = domainObject.qrData
            )
        }
    }
}