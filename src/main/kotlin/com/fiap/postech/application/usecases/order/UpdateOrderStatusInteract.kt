package com.fiap.postech.application.usecases.order

import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.exceptions.NoObjectFoundException
import com.fiap.postech.infrastructure.controller.dto.OrderResponse
import com.fiap.postech.infrastructure.controller.dto.OrderResponse.Companion.toOrderResponse

class UpdateOrderStatusInteract(
    private val orderGateway: OrderGateway
) {
    suspend fun updateOrderStatus(id: String, newStatus: String): OrderResponse {
        return orderGateway.findById(id)?.let {
            orderGateway.save(it.withUpdatedStatus(newStatus)).toOrderResponse()
        } ?: throw NoObjectFoundException("No order found for id = $id")
    }
}