package br.com.fiap.postech.configuration

import br.com.fiap.postech.application.gateways.*
import br.com.fiap.postech.application.usecases.order.GetOrderInteract
import br.com.fiap.postech.application.usecases.order.OrderCheckoutInteract
import br.com.fiap.postech.application.usecases.order.UpdateOrderStatusInteract
import br.com.fiap.postech.application.usecases.payment.CreatePaymentInteract
import br.com.fiap.postech.infrastructure.gateways.*
import br.com.fiap.postech.infrastructure.listener.PaymentStatusUpdateListener
import br.com.fiap.postech.infrastructure.persistence.OrderFacade
import br.com.fiap.postech.infrastructure.persistence.OrderFacadeImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import java.net.URI
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

fun Application.configureKoin(
    config: ApplicationConfig
) {
    install(Koin) {
        modules(module(config))
    }
}

private fun module(config: ApplicationConfig ) = module {
    val paymentServiceURL = config.property("payment_service.host").getString()
    val kitchenServiceURL = config.property("kitchen_service.host").getString()
    val productServiceURL = config.property("product_service.host").getString()
    val customerServiceURL = config.property("customer_service.host").getString()
    val awsConfiguration = AwsConfiguration(config)

    single<SqsClient> {
        val sqsClient = SqsClient.builder()
            .region(Region.of(awsConfiguration.region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(awsConfiguration.accessKey, awsConfiguration.secretAccessKey)
                )
            )
        if (awsConfiguration.account == "000000000000")
            sqsClient.endpointOverride(URI(awsConfiguration.baseUrl))

        sqsClient.build()
    }

    val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    single <SqsGateway> { SqsClientGateway(get(), awsConfiguration) }
    single<HttpClient> { client }
    single<OrderFacade> { OrderFacadeImpl() }
    single<OrderGateway> { OrderGatewayImpl(get()) }
    single<ProductGateway> { ProductClientGateway(get(), productServiceURL) }
    single<KitchenGateway> { KitchenClientGateway(get(), kitchenServiceURL) }
    single<PaymentGateway> { PaymentClientGateway(get(), paymentServiceURL) }
    single<CustomerGateway> { CustomerClientGateway(get(), customerServiceURL) }
    single { OrderCheckoutInteract(get(), get(), get(), get(), get()) }
    single { GetOrderInteract(get()) }
    single { UpdateOrderStatusInteract(get(), get()) }
    single { CreatePaymentInteract(get()) }
    single { PaymentStatusUpdateListener(get(), awsConfiguration, get()) }
}