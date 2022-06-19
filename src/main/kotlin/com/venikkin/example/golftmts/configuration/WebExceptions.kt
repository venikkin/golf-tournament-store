package com.venikkin.example.golftmts.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class WebExceptions {

    companion object {
        val log: Logger = LoggerFactory.getLogger(WebExceptions::class.java)
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequestExceptionHandler(ex: BadRequestException): ResponseEntity<Any> {
        log.warn("Bad request exception", ex)
        return ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
    }

}

class BadRequestException(message: String, t: Throwable? = null): RuntimeException(message, t)