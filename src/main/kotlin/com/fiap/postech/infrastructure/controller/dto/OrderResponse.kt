package com.fiap.postech.infrastructure.controller.dto

import com.fiap.postech.domain.entities.Order
import com.fiap.postech.infrastructure.controller.dto.OrderItemResponse.Companion.toResponse
import java.time.LocalDateTime

class OrderResponse(
    val orderId: Long,
    val cpf: String?,
    val items: List<OrderItemResponse>,
    val status: String,
    val createdAt: LocalDateTime,
    val qrData: String? = null
) {
    companion object {
        fun fromDomain(domainObject: Order) =
            domainObject.id?.let { order ->
                OrderResponse(
                    order,
                    domainObject.customerCpf?.value,
                    domainObject.items.map { orderItem -> OrderItemResponse.fromDomain(orderItem) },
                    domainObject.status.name,
                    domainObject.createdAt,
                    domainObject.qrData
                )
            }

        fun Order.toOrderResponse() =
            this.id?.let {
                OrderResponse(
                    id,
                    customerCpf.toString(),
                    items.map { it.toResponse() },
                    status.name,
                    createdAt,
                    qrData
                )
            }
    }
}