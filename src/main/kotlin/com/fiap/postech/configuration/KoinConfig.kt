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
import com.fiap.postech.infrastructure.persistence.entities.OrderFacade
import com.fiap.postech.infrastructure.persistence.entities.OrderFacadeImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(appModules)
    }
}

val appModules = module {
    single<OrderFacade> { OrderFacadeImpl() }
    single<OrderGateway> { OrderGatewayImpl(get()) }
    single <ProductGateway> { ProductClientGateway() }
    single <KitchenGateway> { KitchenClientGateway() }
    single <PaymentGateway> { PaymentClientGateway() }
    single { OrderCheckoutInteract(get(), get(), get(), get()) }
    single { GetOrderInteract(get()) }
    single { UpdateOrderStatusInteract(get()) }
    single { CreatePaymentInteract(get()) }
}