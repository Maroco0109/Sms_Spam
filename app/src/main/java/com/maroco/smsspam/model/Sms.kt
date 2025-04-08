package com.maroco.smsspam

data class Sms(
    val body: String,
    val modelResults: MutableMap<String, Float> = mutableMapOf()
)