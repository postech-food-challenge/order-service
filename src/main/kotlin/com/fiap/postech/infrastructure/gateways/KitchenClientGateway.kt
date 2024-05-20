package com.fiap.postech.infrastructure.gateways

import com.fiap.postech.application.gateways.KitchenGateway
import com.fiap.postech.domain.entities.OrderItem
import com.fiap.postech.infrastructure.client.kitchen.KitchenResponse
import io.ktor.http.*
import java.util.*

class KitchenClientGateway: KitchenGateway {

    override fun startPreparation(orderId: UUID, items: List<OrderItem>) {
        HttpStatusCode.OK
    }
}