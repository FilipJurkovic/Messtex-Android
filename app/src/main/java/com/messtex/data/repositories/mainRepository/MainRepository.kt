package com.messtex.data.repositories.mainRepository

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.messtex.data.api.RetrofitInstance
import com.messtex.data.models.*
import retrofit2.Response

class MainRepository() {


    suspend fun getUtilizationUnitData(utilizationData: UtilizationData): Response<UserData> {
        return RetrofitInstance.api.getUtilizationUnitData(PostModel(String.format("getUtilizationUnitData(\"%s\")", utilizationData)))
    }

    suspend fun takeMeterReadings(postModelRecord: PostModelRecord): Response<CarbonData> {
        return RetrofitInstance.api.takeMeterReadings(PostModel(String.format("takeMeterReadings(\"%s\")", postModelRecord)))
    }

    suspend fun takeContactForm(contactFormData: ContactFormData) {
        return RetrofitInstance.api.takeContactForm(PostModel(String.format("takeContactForm(\"%s\")", contactFormData)))
    }

    suspend fun getFAQs(): Response<FaqModel> {
        return RetrofitInstance.api.getFAQs(PostModel("getFAQs()"))
    }


}