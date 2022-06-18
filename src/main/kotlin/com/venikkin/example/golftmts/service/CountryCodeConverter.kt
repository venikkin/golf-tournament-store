package com.venikkin.example.golftmts.service

import org.springframework.stereotype.Service

@Service
class CountryCodeConverter {

    fun convertToCountryCode(country: String): String {
        return country
    }

}