package com.fiap.postech.application.gateways

import com.fiap.postech.infrastructure.client.payment.CreatePaymentRequest
import com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse

interface PaymentGateway {

    fun createPayment(request: CreatePaymentRequest) : CreatePaymentResponse
}