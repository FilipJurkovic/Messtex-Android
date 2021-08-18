package com.messtex.data.repositories.mainRepository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.jakewharton.picasso.OkHttp3Downloader
import com.messtex.data.api.RetrofitInstance
import com.messtex.data.models.*
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response


class MainRepository() {

    suspend fun getUtilizationUnitData(utilizationData: UtilizationData): Response<UserData> {
        val body: String = Gson().toJson(utilizationData).toString()
        Log.d(
            "Readings POST request", RetrofitInstance.api.getUtilizationUnitData(
                PostModel(
                    "getUtilizationUnitData(\"${
                        body.replace("\"", "\"\"")
                    }\")"
                )
            ).body().toString()
        )
        return RetrofitInstance.api.getUtilizationUnitData(
            PostModel(
                "getUtilizationUnitData(\"${
                    body.replace("\"", "\"\"")
                }\")"
            )
        )
    }

    suspend fun takeMeterReadings(postModelRecord: PostModelRecord): Response<CarbonData> {
        val body: String = Gson().toJson(postModelRecord).toString()
        // Log.d("Readings POST request", RetrofitInstance.api.takeMeterReadings(PostModel("takeMeterReadings(\"$body\")")).toString())
        return RetrofitInstance.api.takeMeterReadings(PostModel("takeMeterReadings(\"$body\")"))
    }

    suspend fun takeContactForm(contactFormData: ContactFormData) {
        val body: String = Gson().toJson(contactFormData).toString()
        return RetrofitInstance.api.takeContactForm(
            PostModel(
                "takeContactForm(\"${
                    body.replace("\"", "\"\"")
                }\")"
            )
        )
    }

    suspend fun getFAQs(faqData: FaqPostModel): Response<FaqModel> {
        val body: String = Gson().toJson(faqData).toString()
        return RetrofitInstance.api.getFAQs(
            PostModel(
                "getFAQs(\"${
                    body.replace("\"", "\"\"")
                }\")"
            )
        )
    }

    suspend fun getCO2Level(): Response<Double> {
        return RetrofitInstance.api.getCO2Level(PostModel("getCO2Level()"))
    }

    fun getCounterImage(context: Context): Picasso {
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
                .build()
            chain.proceed(newRequest)
        }).build()
        return Picasso.Builder(context).downloader(OkHttp3Downloader(client)).build()
    }
}