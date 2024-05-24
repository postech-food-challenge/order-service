package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.infrastructure.client.product.ProductResponse

interface ProductGateway {
    suspend fun getProduct(id: Long): ProductResponse
}