package br.com.fiap.postech.infrastructure.controller.dto

import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate

@Serializable
data class CheckoutRequest(
    val cpf: String?,
    val items: List<OrderItemRequest>
) {
        init {
            validate(this) {
                if (cpf != null) {
                    validate(CheckoutRequest::cpf).hasSize(11, 11)
                }
                validate(CheckoutRequest::items).isNotEmpty()
            }
        }
}