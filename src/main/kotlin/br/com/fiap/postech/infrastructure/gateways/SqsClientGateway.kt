package br.com.fiap.postech.infrastructure.gateways

import br.com.fiap.postech.application.gateways.SqsGateway
import br.com.fiap.postech.configuration.AwsConfiguration
import br.com.fiap.postech.infrastructure.gateways.dto.StartPreparationDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

class SqsClientGateway(
    private val sqsClient: SqsClient,
    private val awsConfiguration: AwsConfiguration
): SqsGateway {
    override suspend fun startOrderPreparation(dto: StartPreparationDTO) {
        log.info(dto.toString())
        val messageRequest = SendMessageRequest.builder()
            .queueUrl(awsConfiguration.startPreparationQueueUrl)
            .messageBody(Json.encodeToString(dto))
            .build()

        sqsClient.sendMessage(messageRequest)
    }
    companion object {
        val log = LoggerFactory.getLogger(SqsClientGateway::class.java)
    }
}