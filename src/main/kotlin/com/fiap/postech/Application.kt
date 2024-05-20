package com.fiap.postech

import com.fiap.postech.configuration.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val paymentServiceURL = environment.config.property("payment_service.host").getString()
    val kitchenServiceURL = environment.config.property("kitchen_service.host").getString()
    val productServiceURL = environment.config.property("product_service.host").getString()
    configureSerialization()
    DatabaseSingleton.init(environment.config, log)
    configureRouting()
    configureKoin(paymentServiceURL, kitchenServiceURL, productServiceURL)
    configureExceptionsResponse()
}


