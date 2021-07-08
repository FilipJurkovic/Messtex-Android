package com.messtex.data.models

import com.google.gson.annotations.SerializedName

data class PostModelRecord(
    val verificationCode: String,
    val meterReadings : Array<MeterData>,
    val firstName: String,
    val secondName: String,
    val email: String,
    val phone: String,
    val street: String,
    val houseNumber: String,
    val postcode: Int,
    val city: String,
    val floor: String,
    var sendCopy: Boolean,
    val readingReason: String
)