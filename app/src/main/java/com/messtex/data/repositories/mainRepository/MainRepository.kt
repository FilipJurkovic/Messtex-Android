package com.messtex.data.repositories.mainRepository

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.messtex.data.api.RetrofitInstance
import com.messtex.data.models.MeterData
import com.messtex.data.models.PostModel
import com.messtex.data.models.PostModelRecord
import retrofit2.Response

class MainRepository() {


    suspend fun apiPost(postModelRecord: PostModelRecord): Response<PostModel> {
        return RetrofitInstance.api.apiPost(PostModel(postModelRecord))
    }


}