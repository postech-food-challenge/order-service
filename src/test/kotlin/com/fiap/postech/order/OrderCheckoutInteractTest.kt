package com.fiap.postech.order

import br.com.fiap.postech.application.gateways.CustomerGateway
import br.com.fiap.postech.application.gateways.KitchenGateway
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.gateways.ProductGateway
import br.com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import br.com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import br.com.fiap.postech.domain.entities.CPF
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.domain.exceptions.CustomerNotFound
import br.com.fiap.postech.domain.exceptions.PaymentNotCreatedException
import br.com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import br.com.fiap.postech.infrastructure.client.product.ProductResponse
import br.com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import br.com.fiap.postech.infrastructure.controller.dto.OrderItemRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

class OrderCheckoutInteractTest {

    private lateinit var orderGateway: OrderGateway
    private lateinit var productGateway: ProductGateway
    private lateinit var createPaymentInteract: CreatePaymentInteract
    private lateinit var kitchenGateway: KitchenGateway
    private lateinit var orderCheckoutInteract: OrderCheckoutInteract
    private lateinit var customerGateway: CustomerGateway

    @BeforeEach
    fun setUp() {
        orderGateway = mock {}
        productGateway = mock {}
        createPaymentInteract = mock {}
        kitchenGateway = mock {}
        customerGateway = mock {}
        orderCheckoutInteract = OrderCheckoutInteract(
            orderGateway,
            productGateway,
            createPaymentInteract,
            kitchenGateway,
            customerGateway
        )
    }

    @Test
    fun `should successfully complete checkout`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val product = ProductResponse(1L, "Burger", "Delicious", "image.png", 10, "MAIN")
            val orderItemRequest = OrderItemRequest(1L, 2, "Extra cheese", true)
            val checkoutRequest = CheckoutRequest("12345678901", listOf(orderItemRequest))
            val order = Order(orderId.toString(), CPF("12345678901"),
                OrderStatus.CREATED, LocalDateTime.now(), true, 10, "AA")
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
            val order = Order(orderId.toString(), CPF("12345678901"),
                OrderStatus.CREATED, LocalDateTime.now(), true, 10, "AA")
            val createPaymentResponse = CreatePaymentResponse(10, "AA", orderId)

            whenever(productGateway.getProduct(1L)).thenReturn(product)
            whenever(orderGateway.save(any())).thenReturn(order)
            whenever(createPaymentInteract.createPayment(any(), any())).thenReturn(createPaymentResponse)

            val result = orderCheckoutInteract.checkout(checkoutRequest)

            assertNotNull(result)
            assertNotNull(result.orderId)
        }
    }

    @Test
    fun `should not save order when payment fails`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val product = ProductResponse(1L, "Burger", "Delicious", "image.png", 10, "MAIN")

            whenever(productGateway.getProduct(1L)).thenReturn(product)
            whenever(createPaymentInteract.createPayment(any(), any())).thenThrow(PaymentNotCreatedException("Error while creating payment. Order $orderId not created"))

            verify(orderGateway, times(0)).save(any())
        }
    }

    @Test
    fun `should successfully complete checkout when receiving valid CPF`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val cpf = "12345678901"
            val product = ProductResponse(1L, "Burger", "Delicious", "image.png", 10, "MAIN")
            val orderItemRequest = OrderItemRequest(1L, 2, "Extra cheese", true)
            val checkoutRequest = CheckoutRequest(cpf, listOf(orderItemRequest))
            val order = Order(orderId.toString(), CPF(cpf),
                OrderStatus.CREATED, LocalDateTime.now(), true, 10, "AA")
            val createPaymentResponse = CreatePaymentResponse(10, "AA", orderId)

            whenever(customerGateway.checkCustomer(cpf)).thenReturn(Unit)
            whenever(productGateway.getProduct(1L)).thenReturn(product)
            whenever(orderGateway.save(any())).thenReturn(order)
            whenever(createPaymentInteract.createPayment(any(), any())).thenReturn(createPaymentResponse)

            val result = orderCheckoutInteract.checkout(checkoutRequest)

            assertNotNull(result)
            assertEquals(order.id, result.orderId)
            verify(customerGateway, times(1)).checkCustomer(any())
        }
    }

    @Test
    fun `should throw not found exception when complete checkout`() {
        runBlocking {
            val cpf = "12345678901"
            val orderItemRequest = OrderItemRequest(1L, 2, "Extra cheese", true)
            val checkoutRequest = CheckoutRequest(cpf, listOf(orderItemRequest))

            whenever(customerGateway.checkCustomer(cpf)).thenThrow(RuntimeException())

            assertThrows<CustomerNotFound> { orderCheckoutInteract.checkout(checkoutRequest) }
        }
    }
}