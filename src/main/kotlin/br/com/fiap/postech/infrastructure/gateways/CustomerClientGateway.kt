package br.com.fiap.postech.infrastructure.gateways

import br.com.fiap.postech.application.gateways.CustomerGateway
import io.ktor.client.*
import io.ktor.client.request.*

class CustomerClientGateway(val client: HttpClient, val customerServiceURL: String): CustomerGateway {
    override suspend fun checkCustomer(cpf: String) {
        client.get("$customerServiceURL/customer/$cpf")
    }
}