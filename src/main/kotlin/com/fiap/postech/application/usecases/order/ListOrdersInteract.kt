package com.fiap.postech.application.usecases.order

import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.domain.entities.OrderStatus
import com.fiap.postech.domain.exceptions.NoObjectFoundException
import com.fiap.postech.infrastructure.controller.dto.OrderResponse

class ListOrdersInteract(private val orderGateway: OrderGateway) {
    fun getOrders(status: String?): List<OrderResponse> {
        val orders = when {
            status.isNullOrEmpty() -> orderGateway.findActiveOrdersSorted()
            else -> OrderStatus.validateStatus(status)
                .let { orderGateway.findByStatus(it) }
        }

        return orders.takeIf { it.isNotEmpty() }
            ?.mapNotNull { order -> OrderResponse.fromDomain(order) }
            ?: throw NoObjectFoundException("No orders found.")
    }
}