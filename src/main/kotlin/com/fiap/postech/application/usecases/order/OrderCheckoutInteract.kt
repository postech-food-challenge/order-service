package com.fiap.postech.application.usecases.order

import com.fiap.postech.application.gateways.KitchenGateway
import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.application.gateways.ProductGateway
import com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import com.fiap.postech.domain.entities.CPF.Companion.toCpf
import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.entities.OrderItem
import com.fiap.postech.domain.exceptions.OrderNotCreatedInKitchen
import com.fiap.postech.domain.exceptions.PaymentNotCreatedException
import com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import com.fiap.postech.infrastructure.controller.dto.OrderResponse
import java.util.UUID

class OrderCheckoutInteract(
    private val orderGateway: OrderGateway,
    private val productGateway: ProductGateway,
    private val createPaymentInteract: CreatePaymentInteract,
    private val kitchenGateway: KitchenGateway
) {

    suspend fun checkout(request: CheckoutRequest): OrderResponse {
        val customerCpf = request.cpf?.toCpf()
        val orderUuid = UUID.randomUUID()

        val orderItems = request.items.map { orderItemRequest ->
            productGateway.getProduct(orderItemRequest.productId).let {
                OrderItem.create(
                    orderItemRequest.productId,
                    orderItemRequest.quantity,
                    orderItemRequest.observations,
                    orderItemRequest.toGo,
                    it.price
                )
            }
        }

        val createPaymentResponse = runCatching { createPaymentInteract.createPayment(orderItems, orderUuid) }
            .getOrElse { throw PaymentNotCreatedException("Error while creating payment. Order $orderUuid not created") }

        runCatching { kitchenGateway.startPreparation(orderUuid, orderItems) }
            .onFailure { throw OrderNotCreatedInKitchen("Error while sending to kitchen. Order $orderUuid not created") }

        return Order.createOrder(orderUuid, customerCpf, createPaymentResponse).run {
            orderGateway.save(this)
        }.let {
            OrderResponse.fromDomain(it) ?: throw IllegalStateException("Saved order ID should not be null.")
        }
    }
}