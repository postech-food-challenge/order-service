package br.com.fiap.postech.infrastructure.gateways

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.exceptions.OrderNotFoundException
import br.com.fiap.postech.infrastructure.persistence.OrderFacade
import br.com.fiap.postech.infrastructure.persistence.entities.OrderEntity

class OrderGatewayImpl(private val facade: OrderFacade) : OrderGateway {
    override suspend fun save(order: Order): Order {
        OrderEntity.fromDomain(order).let { orderEntity ->
            val savedEntity = facade.insert(orderEntity)
            return Order.fromEntity(savedEntity)
        }
    }

    override suspend fun update(order: Order): Order {
        OrderEntity.fromDomain(order).let { orderEntity ->
            val savedEntity = facade.update(orderEntity)
            return Order.fromEntity(savedEntity)
        }
    }

    override suspend fun findById(id: String): Order {
        return facade.findById(id)?.let { Order.fromEntity(it) }
            ?: throw OrderNotFoundException(id)
    }
}