package com.fiap.postech.configuration

import com.fiap.postech.domain.exceptions.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptionsResponse() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is NoObjectFoundException, is OrderNotFoundException ->
                    call.respond(
                        HttpStatusCode.NotFound,
                        ExceptionResponse("${throwable.message}", HttpStatusCode.NotFound.value)
                    )

                is InvalidParameterException ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ExceptionResponse("${throwable.message}", HttpStatusCode.BadRequest.value)
                    )

                is DatabaseOperationException ->
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ExceptionResponse("${throwable.message}", HttpStatusCode.InternalServerError.value)
                    )
            }
        }
    }
}