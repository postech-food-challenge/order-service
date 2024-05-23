package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.domain.entities.Order

interface OrderGateway {
    suspend fun save(order: Order): Order

    suspend fun findById(id: String): Order?
}