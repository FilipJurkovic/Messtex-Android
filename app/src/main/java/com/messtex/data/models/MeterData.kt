package com.messtex.data.models

import java.io.Serializable

data class MeterData(

    val counterNumber: String,
    val counterType: String,
    val counterValue: String,
    val userMessage: String

): Serializable