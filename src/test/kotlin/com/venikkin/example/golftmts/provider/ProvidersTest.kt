package com.venikkin.example.golftmts.provider

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URL
import java.util.UUID

class ProvidersTest {

    companion object {
        const val EXAMPLE_TOKEN = "999d71a7-b91d-49f6-8f16-c036fccbb69d"
    }

    @Test
    fun `loadProvidersData extracts available providers settings from external URL`() {
        // given
        val providers = Providers(URL("file:conf/providers.example.json"))
        providers.loadProvidersData()

        // when
        providers.loadProvidersData()

        // then
        val providerSettings = providers.getProviderSettingsByToken(EXAMPLE_TOKEN)
        assertNotNull(providerSettings)
        assertEquals("Source1", providerSettings?.alias)
        assertEquals(EXAMPLE_TOKEN, providerSettings?.token)
        assertEquals("source1PayloadConverter", providerSettings?.payloadConverter)
    }

    @Test
    fun `loadProvidersData throws an error when provider list is empty`() {
        // given
        val providers = Providers(URL("file:conf/providers.empty.example.json"))

        // when
        assertThrows(IllegalArgumentException::class.java) { providers.loadProvidersData() }
    }

    @Test
    fun `getProviderSettingsByToken returns null if not provider found`() {
        // given
        val providers = Providers(URL("file:conf/providers.example.json"))
        providers.loadProvidersData()

        // when
        val providerSettings = providers.getProviderSettingsByToken(UUID.randomUUID().toString())

        // then
        assertNull(providerSettings)
    }


}