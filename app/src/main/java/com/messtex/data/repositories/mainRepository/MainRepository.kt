package com.messtex.data.repositories.mainRepository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.messtex.data.api.RetrofitInstance
import com.messtex.data.models.*
import com.messtex.ui.main.view.MainActivity
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import java.io.IOException

class MainRepository() {

    suspend fun getUtilizationUnitData(utilizationData: UtilizationData): Response<UserData> {
        var body : String = Gson().toJson(utilizationData).toString()
        return RetrofitInstance.api.getUtilizationUnitData(PostModel("getUtilizationUnitData(\"${
            body.replace("\"", "\"\"")
        }\")"))
    }

    suspend fun takeMeterReadings(postModelRecord: PostModelRecord): Response<CarbonData> {
        var body : String = Gson().toJson(postModelRecord).toString()
       // Log.d("Readings POST request", RetrofitInstance.api.takeMeterReadings(PostModel("takeMeterReadings(\"$body\")")).toString())
        return RetrofitInstance.api.takeMeterReadings(PostModel("takeMeterReadings(\"$body\")"))
    }

    suspend fun takeContactForm(contactFormData: ContactFormData) {
        var body : String = Gson().toJson(contactFormData).toString()
        return RetrofitInstance.api.takeContactForm(PostModel("takeContactForm(\"${
            body.replace("\"", "\"\"")
        }\")"))
    }

    suspend fun getFAQs(): Response<FaqModel> {
        //Log.d("FAQ request", RetrofitInstance.api.getFAQs(PostModel("getFAQs()")).toString())
        return RetrofitInstance.api.getFAQs(PostModel("getFAQs()"))
    }


}