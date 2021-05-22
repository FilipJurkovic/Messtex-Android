package com.messtex.data.repositories.mainRepository

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.messtex.data.api.RetrofitInstance
import com.messtex.data.models.MeterData
import com.messtex.data.models.PostModel
import com.messtex.data.models.PostModelRecord
import com.messtex.data.models.localdb.User
import com.messtex.data.models.localdb.UserDao
import com.messtex.data.models.localdb.UserDatabase
import retrofit2.Response

class MainRepository(private val db: UserDatabase) {

    private val postModelData = MutableLiveData<PostModelRecord>()

    suspend fun apiPost(): Response<PostModel> {
        return RetrofitInstance.api.apiPost(PostModel(postModelData.value!!))
    }

    fun editPostModelUserData(userData: User) {
        postModelData.postValue(
            PostModelRecord(
                userData.firstName,
                userData.lastName,
                userData.street,
                userData.houseNumber,
                userData.postcode,
                userData.city,
                postModelData.value?.meterId,
                postModelData.value?.meterValue,
                postModelData.value?.meterType,
                "Heizraum"
            )
        )
    }

    fun editPostModelMeterData(meterData: MeterData) {
        postModelData.postValue(
            PostModelRecord(
                postModelData.value?.firstName,
                postModelData.value?.lastName,
                postModelData.value?.street,
                postModelData.value?.homeNumber,
                postModelData.value?.postcode,
                postModelData.value?.city,
                meterData.meterId,
                meterData.meterValue,
                meterData.meterType,
                "Heizraum"
            )
        )
    }

    suspend fun appendUser(user: User) =
        db.getUserDao().appendNewUser(user)


    suspend fun updateUser(user: User) =
        db.getUserDao().updateUser(user)


}