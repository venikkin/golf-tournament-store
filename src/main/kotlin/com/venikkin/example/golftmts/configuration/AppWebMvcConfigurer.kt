package com.venikkin.example.golftmts.configuration

import com.venikkin.example.golftmts.provider.ProviderPayloadConverterResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Service
class AppWebMvcConfigurer @Autowired constructor(
        private val providerPayloadConverterResolver: ProviderPayloadConverterResolver
) : WebMvcConfigurer  {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(providerPayloadConverterResolver)
    }

}