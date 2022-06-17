package com.venikkin.example.golftmts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication

@SpringBootApplication
open class Bootstrap {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Bootstrap::class.java, *args)
        }
    }

}
