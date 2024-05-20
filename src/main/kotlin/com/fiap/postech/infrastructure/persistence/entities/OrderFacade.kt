package com.fiap.postech.infrastructure.persistence.entities

interface OrderFacade {

    suspend fun insert(order: OrderEntity): OrderEntity

    suspend fun findById(id: String): OrderEntity?

}