package com.fiap.postech.infrastructure.gateways

import com.fiap.postech.application.gateways.ProductGateway
import com.fiap.postech.infrastructure.client.product.ProductResponse

class ProductClientGateway : ProductGateway {
    override fun getProduct(id: Long): ProductResponse {
        return ProductResponse(
            id = 1L,
            name = "hamburguer",
            description = "aaa",
            image = "aaa",
            price = 10,
            category = "bbbb"
        )
    }

}