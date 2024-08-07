package br.com.fiap.postech.infrastructure.client.payment

import br.com.fiap.postech.configuration.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
class CreatePaymentResponse (
    val totalAmount: Int,
    val qrData: String,
    @Serializable(with = UUIDSerializer::class)
    val orderId: UUID
)