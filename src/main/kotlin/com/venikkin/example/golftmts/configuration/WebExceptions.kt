package com.venikkin.example.golftmts.configuration

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class WebExceptions {

    @ExceptionHandler(BadRequestException::class)
    fun badRequestExceptionHandler(ex: BadRequestException): ResponseEntity<Any> {
        return ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
    }

}

class BadRequestException(message: String): RuntimeException(message)