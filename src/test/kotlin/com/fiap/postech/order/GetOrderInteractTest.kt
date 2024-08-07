package com.fiap.postech.order

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.usecases.order.GetOrderInteract
import br.com.fiap.postech.domain.entities.CPF
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.domain.entities.OrderStatus
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
                OrderStatus.CREATED, LocalDateTime.now(), true, 10, "AA", creteListOfOrderItemJson())

            whenever(orderGateway.findById(any())).thenReturn(order)

            val result = getOrderInteract.getOrder(orderId.toString())

            Assertions.assertNotNull(result)
            Assertions.assertEquals(order.id, result.orderId)
            Assertions.assertEquals(order.customerCpf?.value, result.cpf)
            Assertions.assertEquals(order.qrData, result.qrData)
        }
    }

    private fun creteListOfOrderItemJson() = Json.encodeToString(
        listOf(
            OrderItem(
                productId = 1,
                observations = "Item 1",
                price = 10,
                quantity = 1,
                toGo = false
            ),
            OrderItem(
                productId = 2,
                observations = "Item 2",
                price = 10,
                quantity = 2,
                toGo = false
            )
        )
    )
}