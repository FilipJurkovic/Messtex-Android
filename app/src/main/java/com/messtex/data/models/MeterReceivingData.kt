package com.messtex.data.models

import java.io.Serializable

data class MeterReceivingData(

    val counterNumber: String,
    val counterType: String,
    val counterRoom: String,
    val counterTypeName: String,
    val counterDescriptionText: String,
    val counterDescriptionImage: String,
    val configuration: MeterConfigurationModel?
) : Serializable