package br.com.fiap.postech.infrastructure.controller

import br.com.fiap.postech.application.usecases.order.GetOrderInteract
import br.com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import br.com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import br.com.fiap.postech.domain.getUuidOrThrow
import br.com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import br.com.fiap.postech.infrastructure.controller.dto.OrderStatusRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRouting() {
    route("/v1/orders") {
        checkout()
        getOrder()
        updateOrder()
    }
}

private fun Route.checkout() {
    val orderCheckoutInteract : OrderCheckoutInteract by inject()

    post("/checkout") {
        val request = call.receive<CheckoutRequest>()

        call.respond(HttpStatusCode.Created, orderCheckoutInteract.checkout(request))
    }
}

private fun Route.getOrder() {
    val getOrdersInteract : GetOrderInteract by inject()

    get("/{orderId}") {
        val orderId = getUuidOrThrow("orderId")

        call.respond(HttpStatusCode.OK,  getOrdersInteract.getOrder(orderId))
    }
}

private fun Route.updateOrder() {
    val updateOrderStatusInteract : UpdateOrderStatusInteract by inject()

    patch("/{orderId}") {
        val orderId = getUuidOrThrow("orderId")
        val request = call.receive<OrderStatusRequest>()

        call.respond(HttpStatusCode.OK, updateOrderStatusInteract.updateOrderStatus(orderId, request.status))
    }
}