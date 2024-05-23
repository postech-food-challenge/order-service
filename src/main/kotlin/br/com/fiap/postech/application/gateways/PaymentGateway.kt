package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.infrastructure.client.payment.CreatePaymentRequest
import br.com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse

interface PaymentGateway {

    suspend fun createPayment(request: CreatePaymentRequest) : CreatePaymentResponse
}