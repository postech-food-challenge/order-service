package com.fiap.postech.infrastructure.controller

import com.fiap.postech.application.usecases.order.GetOrderInteract
import com.fiap.postech.application.usecases.order.ListOrdersInteract
import com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import com.fiap.postech.domain.getUuidOrThrow
import com.fiap.postech.infrastructure.controller.dto.CheckoutRequest
import com.fiap.postech.infrastructure.controller.dto.OrderStatusRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

fun Route.orderRouting(di: DI) {
    route("/v1/orders") {
        getOrders(di)
        checkout(di)
        getOrder(di)
        updateOrder(di)
    }
}

private fun Route.checkout(di: DI) {
    val orderCheckoutInteract = di.direct.instance<OrderCheckoutInteract>()

    post("/checkout") {
        val request = call.receive<CheckoutRequest>()

        call.respond(HttpStatusCode.Created, orderCheckoutInteract.checkout(request))
    }
}

private fun Route.getOrders(di: DI) {
    val listOrderInteract = di.direct.instance<ListOrdersInteract>()

    get {
        val request = call.receive<OrderStatusRequest>()

        call.respond(HttpStatusCode.OK, listOrderInteract.getOrders(request.status))
    }
}

private fun Route.getOrder(di: DI) {
    val getOrdersInteract = di.direct.instance<GetOrderInteract>()

    get("/{orderId}") {
        val orderId = getUuidOrThrow("orderId")

        call.respond(HttpStatusCode.OK,  getOrdersInteract.getOrder(orderId))
    }
}

private fun Route.updateOrder(di: DI) {
    val updateOrderStatusInteract = di.direct.instance<UpdateOrderStatusInteract>()

    patch("/{orderId}") {
        val orderId = getUuidOrThrow("orderId")
        val request = call.receive<OrderStatusRequest>()

        call.respond(HttpStatusCode.OK, updateOrderStatusInteract.updateOrderStatus(orderId, request.status))
    }
}