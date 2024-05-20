package com.fiap.postech.application.usecases.order

import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.domain.exceptions.NoObjectFoundException
import com.fiap.postech.infrastructure.controller.dto.OrderResponse
import com.fiap.postech.infrastructure.controller.dto.OrderResponse.Companion.toOrderResponse

class GetOrderInteract(private val orderGateway: OrderGateway) {

    suspend fun getOrder(orderId: String) : OrderResponse {
        val order = orderGateway.findById(orderId)
        return order?.toOrderResponse() ?: throw NoObjectFoundException("No order found for uuid $orderId")
    }
}