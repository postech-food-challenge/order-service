package br.com.fiap.postech.infrastructure.persistence

import br.com.fiap.postech.infrastructure.persistence.entities.OrderEntity

interface OrderFacade {

    suspend fun insert(order: OrderEntity): OrderEntity

    suspend fun update(order: OrderEntity): OrderEntity

    suspend fun findById(id: String): OrderEntity?

}