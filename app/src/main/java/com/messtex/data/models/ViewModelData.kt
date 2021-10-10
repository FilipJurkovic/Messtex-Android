package com.messtex.data.models

import java.io.Serializable
import java.util.*

data class ViewModelData
    (
    val userData: UserData?,
    val co2_data: CarbonData?,
    val meterData: ArrayList<MeterReadingData>?,
    val utilizationData: UtilizationData?,
    val meterValue: String?,
    val faq: FaqModel?,
    val readingStepsProgress: Array<Boolean>,
    val meterInitModelArray: ArrayList<MeterInitModel>?,
    val meterIndex: Int,
    val isCameraAllowed: Boolean

) : Serializable
