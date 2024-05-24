package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.domain.entities.OrderItem
import java.util.UUID

interface KitchenGateway {

    suspend fun startPreparation(orderId: UUID, items: List<OrderItem>)
}