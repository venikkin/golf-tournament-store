package com.venikkin.example.golftmts.model

import java.time.LocalDate
import java.util.Date

data class Tournament(
        var id: Long? = null,
        val name: String,
        val courseName: String,
        val countryCode: String,
        val startDate: LocalDate,
        val endDate: LocalDate,
        var creationTimestamp: Date? = null,
        val rounds: Int,
        val playerCount: Int? = null,
        val forecast: String? = null,
        val externalId: String,
        val provider: String
) {


    fun onPersist() {
        if (creationTimestamp == null) {
            creationTimestamp = Date();
        }
    }

}