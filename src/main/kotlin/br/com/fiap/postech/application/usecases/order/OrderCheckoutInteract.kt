package br.com.fiap.postech.application.usecases.order

import br.com.fiap.postech.application.gateways.CustomerGateway
import br.com.fiap.postech.application.gateways.KitchenGateway
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.gateways.ProductGateway
import br.com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import br.com.fiap.postech.domain.entities.CPF.Companion.toCpf
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.domain.exceptions.CustomerNotFound
import br.com.fiap.postech.domain.exceptions.OrderNotCreatedInKitchen
import br.com.fiap.postech.domain.exceptions.PaymentNotCreatedException
import br.com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import br.com.fiap.postech.infrastructure.controller.dto.OrderResponse
import java.util.UUID
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OrderCheckoutInteract(
    private val orderGateway: OrderGateway,
    private val productGateway: ProductGateway,
    private val createPaymentInteract: CreatePaymentInteract,
    private val kitchenGateway: KitchenGateway,
    private val customerGateway: CustomerGateway
) {

    suspend fun checkout(request: CheckoutRequest): OrderResponse {
       // val customerCpf = request.cpf?.toCpf()
        val orderUuid = UUID.randomUUID()

//        customerCpf?.runCatching { customerGateway.checkCustomer(this.value) }
//            ?.onFailure { throw CustomerNotFound("Customer with CPF ${customerCpf.value} not found") }

        val orderItems = request.items.map { orderItemRequest ->
            //productGateway.getProduct(orderItemRequest.productId).let {
                OrderItem.create(
                    orderItemRequest.productId,
                    orderItemRequest.quantity,
                    orderItemRequest.observations,
                    orderItemRequest.toGo,
                    10
                )
            //}
        }

        val createPaymentResponse = runCatching { createPaymentInteract.createPayment(orderItems, orderUuid) }
            .getOrElse { throw PaymentNotCreatedException("Error while creating payment. Order $orderUuid not created") }

        return Order.createOrder(orderUuid, null, createPaymentResponse, Json.encodeToString(orderItems)).run {
            orderGateway.save(this)
        }.let {
            OrderResponse.fromDomain(it)
        }
    }
}