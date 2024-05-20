package com.fiap.postech.infrastructure.gateways

import com.fiap.postech.application.gateways.PaymentGateway
import com.fiap.postech.infrastructure.client.payment.CreatePaymentRequest
import com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import io.ktor.http.*
import java.util.*

class PaymentClientGateway: PaymentGateway {
    override fun createPayment(request: CreatePaymentRequest): CreatePaymentResponse {
        HttpStatusCode.OK

        return CreatePaymentResponse(
            totalAmount = 10,
            qrData = "aaa",
            orderId = request.orderId
        )
    }
}