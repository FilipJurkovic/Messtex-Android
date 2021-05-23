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

    suspend fun appendUser(user: User) =
        db.getUserDao().appendNewUser(user)


    suspend fun updateUser(user: User) =
        db.getUserDao().updateUser(user)


}