package br.com.fiap.postech.infrastructure.gateways

import br.com.fiap.postech.application.gateways.PaymentGateway
import br.com.fiap.postech.infrastructure.client.payment.CreatePaymentRequest
import br.com.fiap.postech.infrastructure.client.payment.CreatePaymentResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PaymentClientGateway(val client: HttpClient, val paymentServiceURL: String): PaymentGateway {
    override suspend fun createPayment(request: CreatePaymentRequest): CreatePaymentResponse {
        val reponse: CreatePaymentResponse =
         client.post("$paymentServiceURL/v1/payment") {
            setBody(Json.encodeToString(request))
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }.body()
        println(reponse)
        return reponse
    }
}