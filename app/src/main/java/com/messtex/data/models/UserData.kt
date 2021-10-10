package com.messtex.data.models

import java.io.Serializable

data class UserData(

    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phone: String?,
    val street: String?,
    val houseNumber: String?,
    val postcode: Int?,
    val city: String?,
    val floor: String?,
    val readingReason: String?,
    val meters: Array<MeterReceivingData>?,
    val askForSubscribeNewsletter: Boolean?,
    val message: String?,
) : Serializable

