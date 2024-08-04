package br.com.fiap.postech

import br.com.fiap.postech.configuration.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val paymentServiceURL = environment.config.property("payment_service.host").getString()
    val kitchenServiceURL = environment.config.property("kitchen_service.host").getString()
    val productServiceURL = environment.config.property("product_service.host").getString()
    val customerServiceURL = environment.config.property("customer_service.host").getString()
    DatabaseSingleton.init(environment.config, log)
    configureSerialization()
    configureRouting()
    configureKoin(paymentServiceURL, kitchenServiceURL, productServiceURL, customerServiceURL)
    configureExceptionsResponse()
}


