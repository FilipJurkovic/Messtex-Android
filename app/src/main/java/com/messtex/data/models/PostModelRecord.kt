package com.messtex.data.models

import com.google.gson.annotations.SerializedName

data class PostModelRecord(

    @SerializedName("Vorname") val firstName: String?,
    @SerializedName("Nachname")val lastName: String?,

    @SerializedName("Straße")val street: String?,
    @SerializedName("Hausnummer")val homeNumber: Int?,
    @SerializedName("Postleitzahl")val postcode: Int?,
    @SerializedName("Ort")val city: String?,

    @SerializedName("Zählernummer")val meterId: String?,
    @SerializedName("Zählerstand")val meterValue: String?,
    @SerializedName("Zähler Typ")val meterType: String?


){
    @SerializedName("Raum")val room: String = "Heizraum"
}