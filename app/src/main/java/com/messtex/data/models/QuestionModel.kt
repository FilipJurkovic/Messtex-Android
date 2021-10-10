package com.messtex.data.models

import java.io.Serializable

data class QuestionModel(

    val sortNumber: Int,
    val question: String,
    val answer: String,
) : Serializable