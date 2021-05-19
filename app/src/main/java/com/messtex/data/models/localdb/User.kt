package com.messtex.data.models.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    private var id: Int,

    val firstName: String,
    val secondName: String,
    val emailAddress: String?,

    val houseNumber: Int,
    val postcode: Int,
    val city: String,
    val street: String
)




