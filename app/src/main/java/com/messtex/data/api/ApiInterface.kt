package com.messtex.data.api

import com.messtex.data.models.*
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @Headers("Authorization: Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/bp5lebrgr570/query")
    suspend fun getUtilizationUnitData(
        @Body post: PostModel
    ): Response<UserData>


    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/bp5lebrgr570/query")
    suspend fun takeMeterReadings(
        @Body post: PostModel
    ): Response<CarbonData>

    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/bp5lebrgr570/query")
    suspend fun takeContactForm(
        @Body post: PostModel
    )

    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/bp5lebrgr570/query")
    suspend fun getFAQs(
        @Body post: PostModel
    ): Response<FaqModel>

    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/bp5lebrgr570/query")
    suspend fun getCO2Level(
        @Body post: PostModel
    ): Response<Double>

}