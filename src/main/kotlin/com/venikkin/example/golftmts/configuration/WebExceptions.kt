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

    @ExceptionHandler(InvalidProviderException::class)
    fun invalidProviderExceptionHandler(ex: InvalidProviderException): ResponseEntity<Any> {
        log.warn("Invalid provider", ex)
        return ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidPayloadException::class)
    fun invalidProviderExceptionHandler(ex: InvalidPayloadException): ResponseEntity<Any> {
        log.warn("Invalid payload", ex)
        return ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateExceptionHandler(ex: IllegalStateException): ResponseEntity<Any> {
        log.error("Illegal state", ex)
        return ResponseEntity(mapOf("error" to "Internal Service Error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}

class InvalidProviderException(message: String): RuntimeException(message)
class InvalidPayloadException(message: String, t: Throwable?): RuntimeException(message, t) {
    constructor(message: String): this(message, null)
}