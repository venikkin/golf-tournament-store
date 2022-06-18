package com.venikkin.example.golftmts.provider

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Service
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.bind.support.WebDataBinderFactory
import javax.annotation.Resource

@Service
class ProviderPayloadConverterResolver : HandlerMethodArgumentResolver {

    companion object {
        const val PROVIDER_HEADER = "Provider-Token"
    }

    @Resource
    private lateinit var providers: Providers

    private lateinit var converters: Map<String, ProviderPayloadConverter>

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Provider::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any {
        val providerToken = webRequest.getHeader(PROVIDER_HEADER)
        if (providerToken == null || providerToken.isBlank()) {
            throw java.lang.IllegalArgumentException() // todo specific exception
        }
        val provider = providers.getProviderSettingsByToken(providerToken)
                ?: throw java.lang.IllegalArgumentException() // todo specific exception

        val converter = converters.get(provider.payloadConverter)
                ?: throw java.lang.IllegalArgumentException() // todo specific exception

        return converter
    }

}