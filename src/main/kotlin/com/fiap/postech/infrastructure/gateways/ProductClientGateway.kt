package com.fiap.postech.infrastructure.gateways

import com.fiap.postech.application.gateways.ProductGateway
import com.fiap.postech.infrastructure.client.product.ProductResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ProductClientGateway(val client: HttpClient, val productServiceURL: String) : ProductGateway {
    override suspend fun getProduct(id: Long): ProductResponse {
        return client.get("$productServiceURL/v1/products/$id").body()
    }
}