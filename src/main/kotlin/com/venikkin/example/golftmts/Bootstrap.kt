package com.venikkin.example.golftmts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class Bootstrap {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Bootstrap::class.java, *args)
        }
    }

}
