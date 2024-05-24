package br.com.fiap.postech.infrastructure.controller.dto

import kotlinx.serialization.Serializable

@Serializable
class OrderStatusRequest (
    val status: String
)