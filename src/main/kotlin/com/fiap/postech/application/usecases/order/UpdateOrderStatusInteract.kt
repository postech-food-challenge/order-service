package com.fiap.postech.application.usecases.order

import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.exceptions.NoObjectFoundException

class UpdateOrderStatusInteract(
    private val orderGateway: OrderGateway
) {
    fun updateOrderStatus(id: String, newStatus: String): Order {
        return orderGateway.findById(id)?.let {
            orderGateway.save(it.withUpdatedStatus(newStatus))
        } ?: throw NoObjectFoundException("No order found for id = $id")
    }
}