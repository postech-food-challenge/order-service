package com.fiap.postech.domain

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.util.pipeline.*

private val UUID_REGEX_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$".toRegex()

fun PipelineContext<Unit, ApplicationCall>.getUuidOrThrow(param: String): String {
    val uuid = call.parameters[param]?: throw MissingRequestParameterException(param)
    if (UUID_REGEX_PATTERN.matches(uuid).not()) {
        throw BadRequestException("Parameter '$param' must be a valid UUID")
    }
    return uuid
}