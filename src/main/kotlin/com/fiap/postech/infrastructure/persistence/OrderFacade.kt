package com.fiap.postech.infrastructure.persistence

import com.fiap.postech.infrastructure.persistence.entities.OrderEntity

interface OrderFacade {

    suspend fun insert(order: OrderEntity): OrderEntity

    suspend fun findById(id: String): OrderEntity?

}