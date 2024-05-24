package br.com.fiap.postech.infrastructure.client.payment

import java.util.UUID

class CreatePaymentResponse (
    val totalAmount: Int,
    val qrData: String,
    val orderId: UUID
)