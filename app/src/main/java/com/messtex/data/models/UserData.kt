package com.messtex.data.models

data class UserData(

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

