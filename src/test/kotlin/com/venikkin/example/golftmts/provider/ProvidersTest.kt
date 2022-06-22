package com.venikkin.example.golftmts.provider

import com.venikkin.example.golftmts.configuration.InvalidProviderException
import com.venikkin.example.golftmts.provider.Providers.Companion.PROVIDER_HEADER
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.context.request.WebRequest
import java.net.URL
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class ProvidersTest {

    companion object {
        const val EXAMPLE_TOKEN = "999d71a7-b91d-49f6-8f16-c036fccbb69d"
    }

    @Mock
    private lateinit var testWebRequest: WebRequest

    @Test
    fun `loadProvidersData extracts available providers settings from external URL`() {
        // given
        val providers = Providers(URL("file:conf/providers.example.json"))
        given(testWebRequest.getHeader(PROVIDER_HEADER)).willReturn(EXAMPLE_TOKEN)

        // when
        providers.loadProvidersData()

        // then
        val providerSettings = providers.extractProviderFromWebRequest(testWebRequest)
        assertNotNull(providerSettings)
        assertEquals("Source1", providerSettings.alias)
        assertEquals(EXAMPLE_TOKEN, providerSettings.token)
        assertEquals("source1PayloadConverter", providerSettings.payloadConverter)
    }

    @Test
    fun `loadProvidersData throws an error when provider list is empty`() {
        // given
        val providers = Providers(URL("file:conf/providers.empty.example.json"))

        // when
        assertThrows(IllegalArgumentException::class.java) { providers.loadProvidersData() }
    }

    @Test
    fun `extractProviderFromWebRequest throws an error if provider token header is not provided`() {
        // given
        val providers = Providers(URL("file:conf/providers.example.json"))
        providers.loadProvidersData()

        // when
        assertThrows(InvalidProviderException::class.java) { providers.extractProviderFromWebRequest(testWebRequest) }
    }

    @Test
    fun `extractProviderFromWebRequest throws an error if provider is not found`() {
        // given
        val providers = Providers(URL("file:conf/providers.example.json"))
        providers.loadProvidersData()

        given(testWebRequest.getHeader(PROVIDER_HEADER)).willReturn(UUID.randomUUID().toString())

        // when
        assertThrows(InvalidProviderException::class.java) { providers.extractProviderFromWebRequest(testWebRequest) }
    }

}