package com.messtex.data.api

import com.messtex.data.models.PostModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers(
        "Authorization : Bearer a0450340-b7f5-11eb-b02b-1dfd491d94c8",
        "Content-Type : application/json"
    )
    @POST("posts") //TODO: need to add correct endpoint
    suspend fun apiPost(
        @Body post: PostModel
    ): Response<PostModel>
}