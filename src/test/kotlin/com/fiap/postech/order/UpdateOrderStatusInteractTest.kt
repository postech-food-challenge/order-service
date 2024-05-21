package com.fiap.postech.order

import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.entities.OrderStatus
import com.fiap.postech.domain.exceptions.InvalidParameterException
import com.fiap.postech.domain.exceptions.NoObjectFoundException
import kotlinx.coroutines.runBlocking
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

    @BeforeEach
    fun setUp() {
        orderGateway = mock{}
        updateOrderStatusInteract = UpdateOrderStatusInteract(orderGateway)
    }

    @Test
    fun `should update order status when order exists`() {
        runBlocking {
            val orderId = UUID.randomUUID().toString()
            val existingOrder = Order(id = orderId, status = OrderStatus.CREATED,
                createdAt = LocalDateTime.now()
            )
            val newStatus = OrderStatus.COMPLETED.name
            val updatedOrder = existingOrder.withUpdatedStatus(newStatus)

            whenever(orderGateway.findById(orderId)).thenReturn(existingOrder)
            whenever(orderGateway.save(any())).thenReturn(updatedOrder)

            val result = updateOrderStatusInteract.updateOrderStatus(orderId, newStatus)

            Assertions.assertNotNull(result)
            assertEquals(OrderStatus.COMPLETED.name, result.status)
            verify(orderGateway).save(updatedOrder)
        }
    }


    @Test
    fun `should throw InvalidParameterException for invalid order status`() {
        runBlocking {
            val orderId = UUID.randomUUID().toString()
            val existingOrder = Order(id = orderId, status = OrderStatus.CREATED,
                createdAt = LocalDateTime.now()
            )
            val invalidStatus = "INVALID_STATUS"

            whenever(orderGateway.findById(orderId)).thenReturn(existingOrder)

            assertThrows<InvalidParameterException> {
                updateOrderStatusInteract.updateOrderStatus(orderId, invalidStatus)
            }
        }
    }
}