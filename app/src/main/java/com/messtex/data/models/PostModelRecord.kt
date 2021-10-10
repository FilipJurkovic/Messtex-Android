package com.messtex.data.models

data class PostModelRecord(
    val verificationCode: String,
    val language: String,
    val meterReadings: Array<MeterReadingData>,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    var sendCopy: Boolean,
    val getMeterReadingLetterByEmail: Boolean,
    val subscribeNewsletter: Boolean
)