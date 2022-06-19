package com.venikkin.example.golftmts.provider

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStreamReader
import java.net.URL
import javax.annotation.PostConstruct

@Service
class Providers {

    @Value("\${providers.source}")
    private val providerSource: URL? = null

    private lateinit var settings: ProvidersSettings

    @PostConstruct
    fun loadProvidersData() {
        if (providerSource == null) {
            throw IllegalStateException("Please specify 'provider.source' property")
        }
        settings = providerSource.openStream().use { stream ->
            Gson().fromJson(InputStreamReader(stream), ProvidersSettings::class.java)
        }
        if (settings.providers.isEmpty()) {
            throw IllegalStateException("Provider list is empty")
        }
        println(settings)
    }

    fun getProviderSettingsByToken(token: String): ProviderSettings? =
            settings.providers.find { it.token == token }

    data class ProviderSettings(val alias: String, val token: String, val payloadConverter: String)
    data class ProvidersSettings(val providers: List<ProviderSettings>)


}