package br.com.fiap.postech.infrastructure.client.payment

import br.com.fiap.postech.configuration.utils.UUIDSerializer
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