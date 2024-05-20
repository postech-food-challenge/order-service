package com.fiap.postech

import com.fiap.postech.application.gateways.KitchenGateway
import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.application.gateways.ProductGateway
import com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import com.fiap.postech.domain.entities.CPF
import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.entities.OrderStatus
import com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import com.fiap.postech.infrastructure.client.product.ProductResponse
import com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import com.fiap.postech.infrastructure.controller.dto.OrderItemRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

class OrderCheckoutInteractTest {

    private lateinit var orderGateway: OrderGateway
    private lateinit var productGateway: ProductGateway
    private lateinit var createPaymentInteract: CreatePaymentInteract
    private lateinit var kitchenGateway: KitchenGateway
    private lateinit var orderCheckoutInteract: OrderCheckoutInteract

    @BeforeEach
    fun setUp() {
        orderGateway = mock {}
        productGateway = mock {}
        createPaymentInteract = mock {}
        kitchenGateway = mock {}
        orderCheckoutInteract = OrderCheckoutInteract(orderGateway, productGateway, createPaymentInteract, kitchenGateway)
    }

    @Test
    fun `should successfully complete checkout`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val product = ProductResponse(1L, "Burger", "Delicious", "image.png", 10, "MAIN")
            val orderItemRequest = OrderItemRequest(1L, 2, "Extra cheese", true)
            val checkoutRequest = CheckoutRequest("12345678901", listOf(orderItemRequest))
            val order = Order(orderId.toString(), CPF("12345678901"),OrderStatus.CREATED, LocalDateTime.now(), true, 10, "AA")
            val createPaymentResponse = CreatePaymentResponse(10, "AA", orderId)

            whenever(productGateway.getProduct(1L)).thenReturn(product)
            whenever(orderGateway.save(any())).thenReturn(order)
            whenever(createPaymentInteract.createPayment(any(), any())).thenReturn(createPaymentResponse)

            val result = orderCheckoutInteract.checkout(checkoutRequest)

            assertNotNull(result)
            assertEquals(order.id, result.orderId)
        }
    }

    @Test
    fun `should handle checkout with no customer found`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val product = ProductResponse(1L, "Burger", "Delicious", "image.png", 10, "MAIN")
            val orderItemRequest = OrderItemRequest(1L, 2, "Extra cheese", true)
            val checkoutRequest = CheckoutRequest(null, listOf(orderItemRequest))
            val order = Order(orderId.toString(), CPF("12345678901"),OrderStatus.CREATED, LocalDateTime.now(), true, 10, "AA")
            val createPaymentResponse = CreatePaymentResponse(10, "AA", orderId)

            whenever(productGateway.getProduct(1L)).thenReturn(product)
            whenever(orderGateway.save(any())).thenReturn(order)
            whenever(createPaymentInteract.createPayment(any(), any())).thenReturn(createPaymentResponse)

            val result = orderCheckoutInteract.checkout(checkoutRequest)

            assertNotNull(result)
            assertNotNull(result.orderId)
        }
    }
}