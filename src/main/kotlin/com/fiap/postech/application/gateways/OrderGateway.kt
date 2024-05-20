package com.fiap.postech.application.gateways

import com.fiap.postech.domain.entities.Order

interface OrderGateway {
    suspend fun save(order: Order): Order

    suspend fun findById(id: String): Order?
}