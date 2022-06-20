package com.venikkin.example.golftmts.provider

import com.venikkin.example.golftmts.configuration.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Service
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.bind.support.WebDataBinderFactory

@Service
class ProviderPayloadConverterResolver @Autowired constructor(
        private val providers: Providers,
        private val converters: Map<String, ProviderPayloadConverter>
) : HandlerMethodArgumentResolver {

    companion object {
        const val PROVIDER_HEADER = "Provider-Token"
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(PayloadConverter::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any {
        val providerToken = webRequest.getHeader(PROVIDER_HEADER)
        if (providerToken == null || providerToken.isBlank()) {
            throw BadRequestException("Please specify 'Provider-Token' header")
        }
        val provider = providers.getProviderSettingsByToken(providerToken) ?: throw BadRequestException("Unknown provider")
        return converters[provider.payloadConverter] ?: throw BadRequestException("Unknown provider")
    }

}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PayloadConverter