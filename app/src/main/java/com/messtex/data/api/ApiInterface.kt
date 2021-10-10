package com.messtex.data.api

import com.messtex.data.models.CarbonData
import com.messtex.data.models.FaqModel
import com.messtex.data.models.PostModel
import com.messtex.data.models.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("Authorization: Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/wb8mvouksz8g/query")
    suspend fun getUtilizationUnitData(
        @Body post: PostModel
    ): Response<UserData>


    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/wb8mvouksz8g/query")
    suspend fun takeMeterReadings(
        @Body post: PostModel
    ): Response<CarbonData>

    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/wb8mvouksz8g/query")
    suspend fun takeContactForm(
        @Body post: PostModel
    )

    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/wb8mvouksz8g/query")
    suspend fun getFAQs(
        @Body post: PostModel
    ): Response<FaqModel>

    @Headers("Authorization:Bearer eecf7fd0-cee9-11eb-b752-fde919688281")
    @POST("/v1/teams/yCZezLbXfFAiwR6r3/databases/wb8mvouksz8g/query")
    suspend fun getCO2Level(
        @Body post: PostModel
    ): Response<Double>

}