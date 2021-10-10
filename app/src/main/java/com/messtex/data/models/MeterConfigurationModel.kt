package com.messtex.data.models

import java.io.Serializable

data class MeterConfigurationModel(
    val meterAppearance: String,
    val fractionDigitsAuto: Boolean,
    val integerDigitsAuto: Boolean,
    val numberOfCountersAuto: Boolean,
    val fractionDigits: Int?,
    val integerDigits: Int?,
    val numberOfCounters: Int?
) : Serializable
