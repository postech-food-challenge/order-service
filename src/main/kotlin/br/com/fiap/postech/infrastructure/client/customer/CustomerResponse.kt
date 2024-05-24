package br.com.fiap.postech.infrastructure.client.customer

import br.com.fiap.postech.domain.entities.CPF

data class CustomerResponse (val cpf: CPF, val name: String, val email: String)