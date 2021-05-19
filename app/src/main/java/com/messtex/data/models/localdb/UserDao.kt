package com.messtex.data.models.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.messtex.data.models.localdb.User

@Dao
interface UserDao {

    @Update
    fun updateUser(user: User)

    @Insert
    fun appendNewUser(user: User)

    //TODO: implement querying by id
    //TODO: implement listing all users names and ids - needs to implement LiveData
}