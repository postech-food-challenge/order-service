package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.infrastructure.gateways.dto.StartPreparationDTO

interface SqsGateway {
    suspend fun startOrderPreparation(dto: StartPreparationDTO)
}