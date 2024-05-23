package br.com.fiap.postech.application.usecases.order

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.exceptions.NoObjectFoundException
import br.com.fiap.postech.infrastructure.controller.dto.OrderResponse
import br.com.fiap.postech.infrastructure.controller.dto.OrderResponse.Companion.toOrderResponse

class UpdateOrderStatusInteract(
    private val orderGateway: OrderGateway
) {
    suspend fun updateOrderStatus(id: String, newStatus: String): OrderResponse {
        return orderGateway.findById(id)?.let {
            orderGateway.save(it.withUpdatedStatus(newStatus)).toOrderResponse()
        } ?: throw NoObjectFoundException("No order found for id = $id")
    }
}