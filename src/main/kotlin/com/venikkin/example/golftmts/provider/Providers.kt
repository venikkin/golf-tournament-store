package com.venikkin.example.golftmts.provider

import com.google.gson.Gson
import com.venikkin.example.golftmts.configuration.InvalidProviderException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.context.request.WebRequest
import java.io.InputStreamReader
import java.net.URL
import javax.annotation.PostConstruct

@Service
class Providers constructor(
    @Value("\${providers.source}") private val providerSource: URL
) {

    companion object {
        const val PROVIDER_HEADER = "Provider-Token"
    }

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

    fun extractProviderFromWebRequest(webRequest: WebRequest): ProviderSetting {
        val providerToken = webRequest.getHeader(PROVIDER_HEADER)
        if (providerToken == null || providerToken.isBlank()) {
            throw InvalidProviderException("Please specify 'Provider-Token' header")
        }
        return settings.providers.find { it.token == providerToken } ?: throw InvalidProviderException("Unknown provider")
    }

    data class ProviderSetting(val alias: String, val token: String, val payloadConverter: String)
    data class ProvidersSettings(val providers: List<ProviderSetting>)


}