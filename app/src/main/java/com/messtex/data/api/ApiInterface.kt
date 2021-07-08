package com.messtex.data.api

import com.messtex.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers(
        "Authorization : Bearer eecf7fd0-cee9-11eb-b752-fde919688281",
        "Content-Type : application/json"
    )
    @POST("/yCZezLbXfFAiwR6r3/api/v1/databases/qmz4hgc0o1bh/query HTTP/1.1")
    suspend fun getUtilizationUnitData(
        @Body post: PostModel
    ): Response<UserData>

    @POST("/yCZezLbXfFAiwR6r3/api/v1/databases/qmz4hgc0o1bh/query HTTP/1.1")
    suspend fun takeMeterReadings(
        @Body post: PostModel
    ): Response<CarbonData>

    @POST("/yCZezLbXfFAiwR6r3/api/v1/databases/qmz4hgc0o1bh/query HTTP/1.1")
    suspend fun takeContactForm(
        @Body post: PostModel
    )

    @GET("/yCZezLbXfFAiwR6r3/api/v1/databases/qmz4hgc0o1bh/query HTTP/1.1")
    suspend fun getFAQs(
        @Body post: PostModel
    ): Response<FaqModel>
}