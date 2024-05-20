package com.fiap.postech.infrastructure.client.payment

import com.fiap.postech.configuration.utils.UUIDSerializer
import com.fiap.postech.domain.entities.OrderItem
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreatePaymentRequest(
    @Serializable(with = UUIDSerializer::class)
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