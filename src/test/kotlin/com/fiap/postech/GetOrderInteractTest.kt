package com.fiap.postech

import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.application.usecases.order.GetOrderInteract
import com.fiap.postech.domain.entities.CPF
import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.entities.OrderStatus
import com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import com.fiap.postech.infrastructure.client.product.ProductResponse
import com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import com.fiap.postech.infrastructure.controller.dto.OrderItemRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

class GetOrderInteractTest {
    private lateinit var orderGateway: OrderGateway
    private lateinit var getOrderInteract: GetOrderInteract

    @BeforeEach
    fun setUp() {
        orderGateway = mock {}
        getOrderInteract = GetOrderInteract(orderGateway)
    }

    @Test
    fun `should get order successfully`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val order = Order(orderId.toString(), CPF("12345678901"),
                OrderStatus.CREATED, LocalDateTime.now(), true, 10, "AA")

            whenever(orderGateway.findById(any())).thenReturn(order)

            val result = getOrderInteract.getOrder(orderId.toString())

            Assertions.assertNotNull(result)
            Assertions.assertEquals(order.id, result.orderId)
            Assertions.assertEquals(order.customerCpf?.value, result.cpf)
            Assertions.assertEquals(order.qrData, result.qrData)
        }
    }
}