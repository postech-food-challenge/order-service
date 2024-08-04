package br.com.fiap.postech.application.gateways

interface CustomerGateway {

    suspend fun checkCustomer(cpf: String)
}