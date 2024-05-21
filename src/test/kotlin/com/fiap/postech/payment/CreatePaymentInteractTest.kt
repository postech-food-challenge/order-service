package com.fiap.postech.payment

import com.fiap.postech.application.gateways.PaymentGateway
import com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import com.fiap.postech.domain.entities.OrderItem
import com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import com.fiap.postech.infrastructure.controller.dto.OrderItemRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

class CreatePaymentInteractTest {

    private lateinit var paymentGateway: PaymentGateway
    private lateinit var createPaymentInteract: CreatePaymentInteract

    @BeforeEach
    fun setUp() {
        paymentGateway = mock{}
        createPaymentInteract = CreatePaymentInteract(paymentGateway)
    }

    @Test
    fun `should create payment`(){
        runBlocking {
            val orderUuid = UUID.randomUUID()
            val orderItem = OrderItem(1L, 2, "Extra cheese", true, 10)
            val createPaymentResponse = CreatePaymentResponse(10, "AA", orderUuid)

            whenever(paymentGateway.createPayment(any())).thenReturn(createPaymentResponse)

            val response = createPaymentInteract.createPayment(listOf(orderItem), orderUuid)

            Assertions.assertEquals(response.orderId, createPaymentResponse.orderId)
            Assertions.assertEquals(response.totalAmount, createPaymentResponse.totalAmount)
        }
    }
}