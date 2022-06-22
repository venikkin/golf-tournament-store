package com.venikkin.example.golftmts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * This is a minimal application configuration. At the very minimum production application will need to implement security layer
 * provide default error mappings.
 */
@SpringBootApplication
class Bootstrap {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Bootstrap::class.java, *args)
        }
    }

}
