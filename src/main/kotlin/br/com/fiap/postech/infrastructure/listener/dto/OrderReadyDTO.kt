package br.com.fiap.postech.infrastructure.listener.dto

import br.com.fiap.postech.configuration.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class OrderReadyDTO (
    @Serializable(with = UUIDSerializer::class)
    val orderId: UUID,
    val status: String
)