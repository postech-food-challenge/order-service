package br.com.fiap.postech.configuration

import br.com.fiap.postech.application.gateways.KitchenGateway
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.gateways.PaymentGateway
import br.com.fiap.postech.application.gateways.ProductGateway
import br.com.fiap.postech.application.usecases.order.GetOrderInteract
import br.com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import br.com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import br.com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import br.com.fiap.postech.infrastructure.gateways.KitchenClientGateway
import br.com.fiap.postech.infrastructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infrastructure.gateways.PaymentClientGateway
import br.com.fiap.postech.infrastructure.gateways.ProductClientGateway
import br.com.fiap.postech.infrastructure.persistence.OrderFacade
import br.com.fiap.postech.infrastructure.persistence.OrderFacadeImpl
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