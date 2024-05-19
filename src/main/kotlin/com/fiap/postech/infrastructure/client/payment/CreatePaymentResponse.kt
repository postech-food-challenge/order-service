package com.fiap.postech.infrastructure.client.payment

class CreatePaymentResponse (
    val totalAmount: Int,
    val qrData: String,
    val orderId: Long

)