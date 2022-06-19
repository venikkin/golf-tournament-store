package com.venikkin.example.golftmts.util

import org.springframework.jdbc.core.JdbcTemplate
import java.net.URL

object SchemaHelper {

    fun applySchema(jdbcTemplate: JdbcTemplate) {
        jdbcTemplate.execute(String(URL("file:database/schema.sql").openStream().use { it.readBytes() }))
    }

}