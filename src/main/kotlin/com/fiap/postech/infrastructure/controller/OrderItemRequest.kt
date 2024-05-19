package com.fiap.postech.infrastructure.controller

import org.valiktor.validate
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isPositive

class OrderItemRequest(
    val productId: Long,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean
) {
    init {
        validate(this) {
            validate(OrderItemRequest::productId).isNotNull()
            validate(OrderItemRequest::quantity).isPositive()
            validate(OrderItemRequest::toGo).isNotNull()
        }
    }
}