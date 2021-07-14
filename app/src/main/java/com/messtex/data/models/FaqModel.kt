package com.messtex.data.models

import java.io.Serializable

data class FaqModel(

    val faqs: Array<QuestionModel>
) : Serializable