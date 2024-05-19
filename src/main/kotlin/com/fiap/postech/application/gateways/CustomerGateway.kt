package com.fiap.postech.application.gateways

import com.fiap.postech.infrastructure.client.customer.CustomerResponse

interface CustomerGateway {

    fun findByCpf(cpf: String): CustomerResponse
}