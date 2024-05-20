package com.fiap.postech.application.gateways

import com.fiap.postech.domain.entities.OrderItem
import com.fiap.postech.infrastructure.client.kitchen.KitchenResponse
import java.util.UUID

interface KitchenGateway {

    suspend fun startPreparation(orderId: UUID, items: List<OrderItem>)
}