package br.com.fiap.postech.infrastructure.gateways.dto

import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class StartPreparationDTO(
    val orderId: String,
    val orderItems: List<OrderItem>
) {
    companion object {
        fun fromOrder(order: Order, orderItems: List<OrderItem>): StartPreparationDTO {
            return StartPreparationDTO(order.id, orderItems)
        }
    }

    override fun toString(): String {
        return Json.encodeToString(this)
    }
}