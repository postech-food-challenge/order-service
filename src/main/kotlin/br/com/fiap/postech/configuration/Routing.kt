package br.com.fiap.postech.configuration

import br.com.fiap.postech.infrastructure.controller.orderRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        orderRouting()

        get("/health") {
            call.respond(HttpStatusCode.MultiStatus)
        }
    }
}
