package com.fiap.postech.infrastructure.persistence.entities

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fiap.postech.domain.entities.Order
//import io.ktor.features.json.Json
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
//import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Orders : LongIdTable("orders") {
    val customerCpf = varchar("customer_cpf", 255).nullable()
    //val itemsData = jsonb("items_data", JsonNode::class.java)
    val status = varchar("status", 255)
    //val createdAt = datetime("created_at")
    val paymentValidated = bool("payment_validated").nullable()
    val price = integer("price").nullable()
    val qrData = varchar("qr_data", 255).nullable()
    val inStoreOrderId = varchar("in_store_order_id", 255).nullable()

    fun fromDomain(domainObject: Order, objectMapper: ObjectMapper): OrderEntity {
        val itemsData = objectMapper.valueToTree<JsonNode>(domainObject.items)
        return OrderEntity(
            id = domainObject.id,
            customerCpf = domainObject.customerCpf?.value,
            itemsData = itemsData,
            status = domainObject.status.name,
            createdAt = domainObject.createdAt,
            paymentValidated = domainObject.paymentValidated,
            price = domainObject.price,
            qrData = domainObject.qrData
        )
    }
}

data class OrderEntity(
    val id: Long?,
    val customerCpf: String?,
    val itemsData: JsonNode,
    val status: String,
    val createdAt: LocalDateTime,
    val paymentValidated: Boolean?,
    val price: Int?,
    val qrData: String?
) {
    companion object {
        fun fromDomain(domainObject: Order, objectMapper: ObjectMapper): OrderEntity {
            val itemsData = objectMapper.valueToTree<JsonNode>(domainObject.items)
            return OrderEntity(
                id = domainObject.id,
                customerCpf = domainObject.customerCpf?.value,
                itemsData = itemsData,
                status = domainObject.status.name,
                createdAt = domainObject.createdAt,
                paymentValidated = domainObject.paymentValidated,
                price = domainObject.price,
                qrData = domainObject.qrData
            )
        }

//        fun fromRow(row: ResultRow): OrderEntity {
//            return OrderEntity(
//                id = row[Orders.id].value,
//                customerCpf = row[Orders.customerCpf],
//                itemsData = row[Orders.itemsData],
//                status = row[Orders.status],
//                createdAt = row[Orders.createdAt],
//                paymentValidated = row[Orders.paymentValidated],
//                price = row[Orders.price],
//                qrData = row[Orders.qrData],
//                inStoreOrderId = row[Orders.inStoreOrderId]
//            )
//        }
    }
}