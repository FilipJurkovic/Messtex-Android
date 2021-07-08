package com.messtex.data.models

data class FaqModel(

    val faqs: Array<QuestionModel>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FaqModel

        if (!faqs.contentEquals(other.faqs)) return false

        return true
    }

    override fun hashCode(): Int {
        return faqs.contentHashCode()
    }
}
