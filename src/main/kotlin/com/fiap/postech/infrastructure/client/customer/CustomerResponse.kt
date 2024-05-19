package com.fiap.postech.infrastructure.client.customer

import com.fiap.postech.domain.entities.CPF

data class CustomerResponse (val cpf: CPF, val name: String, val email: String)