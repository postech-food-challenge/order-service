package br.com.fiap.postech.configuration

import io.ktor.server.config.ApplicationConfig

class AwsConfiguration(
    val account: String,
    val region: String,
    val baseUrl: String,
    val accessKey: String,
    val secretAccessKey: String,
    val paymentStatusUpdateQueueUrl: String
) {
    constructor (config: ApplicationConfig) : this (
        config.property("aws.account").getString(),
        config.property("aws.region").getString(),
        config.property("aws.base_url").getString(),
        config.property("aws.access_key").getString(),
        config.property("aws.secret_access_key").getString(),
        config.property("aws.queue.payment_status_update_url").getString()
    )
}