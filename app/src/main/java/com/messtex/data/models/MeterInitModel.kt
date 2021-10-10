package com.messtex.data.models

import com.pixolus.meterreading.MeterAppearance
import java.io.Serializable

data class MeterInitModel(
    val meterAppearance: MeterAppearance,
    val fractionDigits: Int,
    val integerDigits: Int,
    val numberOfCounters: Int
) : Serializable
