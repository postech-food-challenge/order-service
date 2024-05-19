package com.fiap.postech.application.usecases.order

import com.fiap.postech.application.gateways.CustomerGateway
import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.application.gateways.ProductGateway
import com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import com.fiap.postech.domain.entities.Order
import com.fiap.postech.domain.entities.OrderItem
import com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import com.fiap.postech.infrastructure.controller.dto.OrderResponse
import java.util.UUID

class OrderCheckoutInteract(
    private val orderGateway: OrderGateway,
    private val customerGateway: CustomerGateway,
    private val productGateway: ProductGateway,
    private val createPaymentInteract: CreatePaymentInteract
) {

    fun checkout(request: CheckoutRequest): OrderResponse {
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

        val orderUuid = UUID.randomUUID()
        val customerCpf = request.cpf?.let(customerGateway::findByCpf)?.cpf
        val createPaymentResponse = createPaymentInteract.createPayment(orderItems, orderUuid)

        //TODO: start de kitchen

        return Order.createOrder(customerCpf, orderItems, createPaymentResponse).run {
            orderGateway.save(this)
        }.let {
            OrderResponse.fromDomain(it) ?: throw IllegalStateException("Saved order ID should not be null.")
        }
    }
}