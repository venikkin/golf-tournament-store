package com.venikkin.example.golftmts.provider

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStreamReader
import java.net.URL
import javax.annotation.PostConstruct

@Service
class Providers constructor(
    @Value("\${providers.source}") private val providerSource: URL
) {

    private lateinit var settings: ProvidersSettings

    @PostConstruct
    fun loadProvidersData() {
        settings = providerSource.openStream().use { stream ->
            Gson().fromJson(InputStreamReader(stream), ProvidersSettings::class.java)
        }
        if (settings.providers.isEmpty()) {
            throw IllegalArgumentException("Provider list is empty")
        }
    }

    fun getProviderSettingsByToken(token: String): ProviderSettings? =
            settings.providers.find { it.token == token }

    data class ProviderSettings(val alias: String, val token: String, val payloadConverter: String)
    data class ProvidersSettings(val providers: List<ProviderSettings>)


}