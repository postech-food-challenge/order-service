package br.com.fiap.postech.infrastructure.controller.dto

import kotlinx.serialization.Serializable
import org.valiktor.validate
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isPositive

@Serializable
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