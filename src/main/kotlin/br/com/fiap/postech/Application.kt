package br.com.fiap.postech

import br.com.fiap.postech.configuration.*
import br.com.fiap.postech.infrastructure.listener.PaymentStatusUpdateListener
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init(environment.config, log)
    configureSerialization()
    configureRouting()
    configureKoin(environment.config)
    configureExceptionsResponse()

    val paymentStatusUpdateListener by inject<PaymentStatusUpdateListener>()
    paymentStatusUpdateListener.startListening()
}