package com.fiap.postech.application.gateways

import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.entities.OrderStatus
import java.util.UUID

interface OrderGateway {
    fun save(order: Order): Order

    fun findAll(): List<Order>

    fun findByStatus(status: OrderStatus): List<Order>

    fun findActiveOrdersSorted(): List<Order>

    fun findById(id: String): Order?
}