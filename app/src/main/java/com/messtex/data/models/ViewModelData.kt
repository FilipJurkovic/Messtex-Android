package com.messtex.data.models

import java.io.Serializable
import java.util.ArrayList

data class ViewModelData
    (
    val userData: UserData?,
    val co2_data: CarbonData?,
    val meterData: ArrayList<MeterData>?,
    val utilizationData: UtilizationData?,
    val meterValue: String?,
    val faq: FaqModel?,

    val heat_meter_step : Boolean,
    val water_meter_step : Boolean,
    val heat_allocator_step: Boolean,
    val contact_step : Boolean,
    val steps_finished : Boolean,

    val meterIndex: Int,
    val isCameraAllowed : Boolean

):Serializable
