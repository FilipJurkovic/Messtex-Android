package com.messtex.data.models

import java.io.Serializable

data class MeterReadingData(

    val counterNumber: String,
    val counterType: String,
    val counterValue: Double,
    val userMessage: String

): Serializable

