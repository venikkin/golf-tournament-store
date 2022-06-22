package com.venikkin.example.golftmts.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Service
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.bind.support.WebDataBinderFactory

/**
 * This service resolves provider settings and injects them into the annotated argument.
 * In the scope of this task, it's possible to parse the payload according to provider's format and inject the payload instead.
 * However, in general, if there will be more provider-specific distinctions, it could be beneficial to keep provider resolution and delete more specific
 * operation (like payload conversion) to more specific services.
 */
@Service
class ProviderResolver @Autowired constructor(
        private val providers: Providers,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Provider::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any {
        return providers.extractProviderFromWebRequest(webRequest)
    }

}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Provider