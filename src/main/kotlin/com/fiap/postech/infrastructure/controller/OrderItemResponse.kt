package com.fiap.postech.infrastructure.controller

import com.fiap.postech.domain.entities.OrderItem

class OrderItemResponse(
    val productId: Long,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean
) {
    companion object {
        fun fromDomain(domainObject: OrderItem) = OrderItemResponse(
            domainObject.productId,
            domainObject.quantity,
            domainObject.observations,
            domainObject.toGo
        )
    }
}