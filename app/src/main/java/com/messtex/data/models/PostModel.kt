package com.messtex.data.models

data class PostModel(

    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: Int?,

    val street: String,
    val homeNumber: Int,
    val postcode: Int,
    val city: String,

    val meterId: String,
    val meterValue: Double,
    val meterType: MeterType

)