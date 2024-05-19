package com.fiap.postech.infrastructure.gateways

import com.fiap.postech.application.gateways.PaymentGateway
import com.fiap.postech.infrastructure.client.payment.CreatePaymentRequest
import com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse

class PaymentClientGateway: PaymentGateway {
    override fun createPayment(request: CreatePaymentRequest): CreatePaymentResponse {
        TODO("Not yet implemented")
    }
}