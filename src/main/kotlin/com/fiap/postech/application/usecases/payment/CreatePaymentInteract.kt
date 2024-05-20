package com.fiap.postech.application.usecases.payment

import com.fiap.postech.application.gateways.PaymentGateway
import com.fiap.postech.domain.entities.OrderItem
import com.fiap.postech.infrastructure.client.payment.CreatePaymentRequest
import com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import java.util.UUID

class CreatePaymentInteract(
    private val paymentGateway: PaymentGateway
) {

    suspend fun createPayment(orderItems: List<OrderItem>, orderUuid: UUID): CreatePaymentResponse {
        val price = calculatePaymentAmount(orderItems)

        return  CreatePaymentRequest.fromDomain(price, orderUuid)
            .let { request -> paymentGateway.createPayment(request) }
    }

    private fun calculatePaymentAmount(orderItems: List<OrderItem>): Int {
        var totalAmount = 0

        orderItems.forEach { orderItem ->
            totalAmount += orderItem.quantity * orderItem.price
        }

        return totalAmount
    }}