package br.com.fiap.postech.domain.entities

import br.com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import br.com.fiap.postech.infrastructure.persistence.entities.OrderEntity
import java.time.LocalDateTime
import java.util.UUID

data class Order(
    val id: String,
    val customerCpf: CPF? = null,
    val status: OrderStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val paymentValidated: Boolean? = null,
    val price: Int? = null,
    val qrData: String? = null,
    val orderItemsJson: String
) {
    fun withUpdatedStatus(newStatus: String): Order {
        val updatedStatus = OrderStatus.validateStatus(newStatus)
        return this.copy(status = updatedStatus)
    }

    fun withUpdatedPayment(newPaymentStatus: Boolean): Order {
        return this.copy(paymentValidated = newPaymentStatus)
    }

    companion object {
        fun createOrder(orderUuid: UUID, customerId: CPF?, createPaymentResponse: CreatePaymentResponse, orderItemsJson: String): Order {
            return Order(
                id = orderUuid.toString(),
                customerCpf = customerId,
                status = OrderStatus.CREATED,
                paymentValidated = false,
                price = createPaymentResponse.totalAmount,
                qrData = createPaymentResponse.qrData,
                orderItemsJson = orderItemsJson
            )
        }

        fun fromEntity(entity: OrderEntity): Order {
            return Order(
                entity.id,
                entity.customerCpf?.let { CPF(it) },
                OrderStatus.valueOf(entity.status),
                entity.createdAt,
                entity.paymentValidated,
                entity.price,
                entity.qrData,
                entity.orderItemsJson
            )
        }
    }
}