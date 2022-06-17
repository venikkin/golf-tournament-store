package com.venikkin.example.golftmts.provider

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Service
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import javax.annotation.PostConstruct
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.bind.support.WebDataBinderFactory
import java.net.URL

@Service
class ProviderPayloadConverterResolver : HandlerMethodArgumentResolver {

    @Value("\${providers-source}")
    private val providerSource: URL? = null

    @PostConstruct
    fun loadProvidersData() {
        // tbd
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation<Provider>(Provider::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory): Any {
        return ""
    }
}