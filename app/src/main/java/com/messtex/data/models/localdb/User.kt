package com.messtex.data.models.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val firstName: String,
    val lastName: String,

    val houseNumber: Int,
    val postcode: Int,
    val city: String,
    val street: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}




