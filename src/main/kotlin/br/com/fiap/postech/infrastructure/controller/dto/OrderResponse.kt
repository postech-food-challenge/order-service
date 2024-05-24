package br.com.fiap.postech.infrastructure.controller.dto

import br.com.fiap.postech.domain.entities.Order
import kotlinx.serialization.Serializable

@Serializable
class OrderResponse(
    val orderId: String,
    val cpf: String?,
    val status: String,
    val createdAt: String,
    val qrData: String? = null
) {
    companion object {
        fun fromDomain(domainObject: Order) =
            OrderResponse(
                domainObject.id,
                domainObject.customerCpf?.value,
                domainObject.status.name,
                domainObject.createdAt.toString(),
                domainObject.qrData
            )

        fun Order.toOrderResponse() =
            OrderResponse(
                id,
                customerCpf?.value.toString(),
                status.name,
                createdAt.toString(),
                qrData
            )
    }
}