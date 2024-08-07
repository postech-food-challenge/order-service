package br.com.fiap.postech.application.usecases.order

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.gateways.SqsGateway
import br.com.fiap.postech.domain.exceptions.NoObjectFoundException
import br.com.fiap.postech.domain.exceptions.OrderNotCreatedInKitchen
import br.com.fiap.postech.infrastructure.controller.dto.OrderResponse
import br.com.fiap.postech.infrastructure.controller.dto.OrderResponse.Companion.toOrderResponse
import br.com.fiap.postech.infrastructure.gateways.dto.StartPreparationDTO
import kotlinx.serialization.json.Json

class UpdateOrderStatusInteract(
    private val orderGateway: OrderGateway,
    private val sqsGateway: SqsGateway
) {
    suspend fun updateOrderStatus(id: String, newStatus: String): OrderResponse {
        val order = orderGateway.findById(id)?.let {
            orderGateway.save(it.withUpdatedStatus(newStatus))
        } ?: throw NoObjectFoundException("No order found for id = $id")

        if (newStatus == "PAYMENT_CONFIRMED")
            sqsGateway.startOrderPreparation(StartPreparationDTO.fromOrder(order, Json.decodeFromString(order.orderItemsJson)))
        return order.toOrderResponse()
    }
}