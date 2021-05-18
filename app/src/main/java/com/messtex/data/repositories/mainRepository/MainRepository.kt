package com.messtex.data.repositories.mainRepository

import com.messtex.data.api.RetrofitInstance
import com.messtex.data.models.PostModel
import retrofit2.Response

class MainRepository {
    suspend fun apiPost(post: PostModel): Response<PostModel> {
        return RetrofitInstance.api.apiPost(post)
    }
}