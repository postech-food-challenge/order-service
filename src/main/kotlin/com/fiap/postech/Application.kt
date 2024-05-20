package com.fiap.postech

import com.fiap.postech.configuration.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val paymentServiceURL = environment.config.property("payment_service.host").getString()
    val kitchenServiceURL = environment.config.property("kitchen_service.host").getString()
    val productServiceURL = environment.config.property("product_service.host").getString()
    DatabaseSingleton.init(environment.config, log)
    configureSerialization()
    configureRouting()
    configureKoin(paymentServiceURL, kitchenServiceURL, productServiceURL)
    configureExceptionsResponse()
}


