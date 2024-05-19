package com.fiap.postech.infrastructure.controller

import com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

fun Route.createOrder(di: DI) {
    route("/v1/orders") {


        route("/checkout") {
            checkout(di)
        }

        route("/{orderId}") {
            getOrder(di)
            updateOrder(di)
        }
    }
}

private fun Route.checkout(di: DI) {
    val orderCheckoutInteract = di.direct.instance<OrderCheckoutInteract>()

    post {
        val request = call.receive<CheckoutRequest>()

        call.respond(HttpStatusCode.OK, orderCheckoutInteract.checkout(request))
    }
}

private fun Route.getOrder(di: DI) {
    get {

    }
}

private fun Route.updateOrder(di: DI) {
    patch {

    }
}