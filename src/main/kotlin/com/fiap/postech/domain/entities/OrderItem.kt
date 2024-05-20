package com.fiap.postech.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val productId: Long,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean,
    val price: Int,
    ) {
    companion object {
        fun create(productId: Long, quantity: Int, observations: String?, toGo: Boolean, price: Int) =
            OrderItem(productId, quantity, observations, toGo, price)
    }
}