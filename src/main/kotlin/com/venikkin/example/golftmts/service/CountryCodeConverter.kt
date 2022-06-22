package com.venikkin.example.golftmts.service

import org.springframework.stereotype.Service

/**
 * This is a stub implementation of the service converting provider-specific country name to two-letter country code.
 * Option for production could be to build a country to country code dictionary and persist in the database.
 * It's expected that information will not be frequently updated, so in-memory caching would be beneficial.
 */
@Service
class CountryCodeConverter {

    fun convertToCountryCode(country: String): String {
        if ("United States Of America" == country) {
            return "US"
        }
        return country
    }

}