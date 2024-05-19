package com.fiap.postech.infrastructure.client.payment

import com.fiap.postech.domain.entities.OrderItem
import java.util.UUID

data class CreatePaymentRequest(
    val orderId: UUID,
    val totalAmount: Int,
    val description: String,
) {
    companion object {
        fun fromDomain(price: Int, orderUuid: UUID) =
            CreatePaymentRequest(
                orderId = orderUuid,
                totalAmount = price,
                description = "Pedido $orderUuid no valor total de $price"
            )
    }
}