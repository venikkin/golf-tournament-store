package com.venikkin.example.golftmts.provider

import com.google.gson.Gson
import com.venikkin.example.golftmts.configuration.InvalidProviderException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.context.request.WebRequest
import java.io.InputStreamReader
import java.net.URL
import javax.annotation.PostConstruct

/**
 * We cannot define provider by publicly available information. This is an example approach by using provider-specific secrets sent via HTTP headers.
 *
 * In the production-ready application, this could be done based on authentication used by specific provider. For example, it could be extracted form IAM role
 * used to sign a request or by a secret from JWT.
 *
 * If token solution is used, token must be stored in secured location (AWS Secretsmanager / AWS SSM parameter store / Database). Automatic token rotation
 * would be also beneficial.
 *
 * This implementation loads provider form from URL provided via configuration. Depending on the requirements,
 * the source of provider settings could be a database. In this case, the schema could be modified as well (see schema comments).
 */
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