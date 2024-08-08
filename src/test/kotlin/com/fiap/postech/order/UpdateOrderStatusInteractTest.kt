package com.fiap.postech.order

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.gateways.SqsGateway
import br.com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.domain.exceptions.InvalidParameterException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

class UpdateOrderStatusInteractTest {

    private lateinit var orderGateway: OrderGateway
    private lateinit var updateOrderStatusInteract: UpdateOrderStatusInteract
    private lateinit var sqsGateway: SqsGateway

    @BeforeEach
    fun setUp() {
        orderGateway = mock {}
        sqsGateway = mock {}
        updateOrderStatusInteract = UpdateOrderStatusInteract(orderGateway, sqsGateway)
    }

    @Test
    fun `should update order status when order exists`() {
        runBlocking {
            val orderId = UUID.randomUUID().toString()
            val existingOrder = Order(
                id = orderId, status = OrderStatus.CREATED,
                createdAt = LocalDateTime.now(),
                orderItemsJson = creteListOfOrderItemJson()
            )
            val newStatus = OrderStatus.COMPLETED.name
            val updatedOrder = existingOrder.withUpdatedStatus(newStatus)

            whenever(orderGateway.findById(orderId)).thenReturn(existingOrder)
            whenever(orderGateway.update(any())).thenReturn(updatedOrder)

            val result = updateOrderStatusInteract.updateOrderStatus(orderId, newStatus)

            Assertions.assertNotNull(result)
            assertEquals(OrderStatus.COMPLETED.name, result.status)
            verify(orderGateway).update(updatedOrder)
        }
    }


    @Test
    fun `should throw InvalidParameterException for invalid order status`() {
        runBlocking {
            val orderId = UUID.randomUUID().toString()
            val existingOrder = Order(
                id = orderId, status = OrderStatus.CREATED,
                createdAt = LocalDateTime.now(),
                orderItemsJson = creteListOfOrderItemJson()
            )
            val invalidStatus = "INVALID_STATUS"

            whenever(orderGateway.findById(orderId)).thenReturn(existingOrder)

            assertThrows<InvalidParameterException> {
                updateOrderStatusInteract.updateOrderStatus(orderId, invalidStatus)
            }
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