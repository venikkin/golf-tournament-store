package com.venikkin.example.golftmts.provider

import com.venikkin.example.golftmts.model.Tournament

interface ProviderPayloadConverter {

    fun convert(payload: String): Tournament

}