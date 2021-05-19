package com.messtex.data.repositories.mainRepository

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.messtex.data.api.RetrofitInstance
import com.messtex.data.models.PostModel
import com.messtex.data.models.localdb.User
import com.messtex.data.models.localdb.UserDao
import com.messtex.data.models.localdb.UserDatabase
import retrofit2.Response

class MainRepository(private val userDao: UserDao) {

    suspend fun apiPost(post: PostModel): <PostModel> {
        return RetrofitInstance.api.apiPost(post)
    }

    @Suppress("RedundantSuspendModifier")

    @WorkerThread
    suspend fun appendUser(user: User) {
        userDao.appendNewUser(user)
    }

    @WorkerThread
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}