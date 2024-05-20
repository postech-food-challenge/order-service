package com.fiap.postech.configuration

import com.fiap.postech.application.gateways.KitchenGateway
import com.fiap.postech.application.gateways.OrderGateway
import com.fiap.postech.application.gateways.PaymentGateway
import com.fiap.postech.application.gateways.ProductGateway
import com.fiap.postech.application.usecases.order.GetOrderInteract
import com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import com.fiap.postech.infrastructure.gateways.KitchenClientGateway
import com.fiap.postech.infrastructure.gateways.OrderGatewayImpl
import com.fiap.postech.infrastructure.gateways.PaymentClientGateway
import com.fiap.postech.infrastructure.gateways.ProductClientGateway
import com.fiap.postech.infrastructure.persistence.OrderFacade
import com.fiap.postech.infrastructure.persistence.OrderFacadeImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    paymentServiceURL: String,
    kitchenServiceURL: String,
    productServiceURL: String
) {
    install(Koin) {
        modules(module(paymentServiceURL, kitchenServiceURL, productServiceURL))
    }
}

private fun module(paymentServiceURL: String, kitchenServiceURL: String, productServiceURL: String) = module {
    val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    single<HttpClient> { client }
    single<OrderFacade> { OrderFacadeImpl() }
    single<OrderGateway> { OrderGatewayImpl(get()) }
    single<ProductGateway> { ProductClientGateway(get(), productServiceURL) }
    single<KitchenGateway> { KitchenClientGateway(get(), kitchenServiceURL) }
    single<PaymentGateway> { PaymentClientGateway(get(), paymentServiceURL) }
    single { OrderCheckoutInteract(get(), get(), get(), get()) }
    single { GetOrderInteract(get()) }
    single { UpdateOrderStatusInteract(get()) }
    single { CreatePaymentInteract(get()) }
}