package br.com.fiap.postech.infrastructure.gateways

import br.com.fiap.postech.application.gateways.KitchenGateway
import br.com.fiap.postech.domain.entities.OrderItem
import io.ktor.client.*
import io.ktor.client.request.*
import java.util.*

class KitchenClientGateway(val client: HttpClient, val kitchenServiceURL: String): KitchenGateway {

    override suspend fun startPreparation(orderId: UUID, items: List<OrderItem>) {
        client.post("$kitchenServiceURL/v1/kitchen/orders/$orderId/start") {
            setBody(listOf(OrderItem))
        }
    }
}