package com.fiap.postech.application.gateways

import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.entities.OrderStatus
import com.fiap.postech.infrastructure.persistence.entities.OrderFacade
import java.util.UUID

interface OrderGateway {
    suspend fun save(order: Order): Order

    suspend fun findById(id: String): Order?
}