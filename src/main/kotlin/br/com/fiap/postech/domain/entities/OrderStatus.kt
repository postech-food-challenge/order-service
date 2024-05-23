package br.com.fiap.postech.domain.entities

import br.com.fiap.postech.domain.exceptions.InvalidParameterException

enum class OrderStatus {
    CREATED, PAYMENT_CONFIRMED, PAYMENT_DENIED, IN_PREPARATION, READY, COMPLETED, CANCELED;

    companion object {
        fun validateStatus(status: String): OrderStatus {
            return enumValues<OrderStatus>().find { it.name == status }
                ?: throw InvalidParameterException("Invalid status: $status")
        }
    }
}