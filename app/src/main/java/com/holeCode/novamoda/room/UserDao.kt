package com.holeCode.novamoda.room

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.holeCode.novamoda.pojo.User

@Dao
interface UserDao {
    @Insert
    fun insertAllUser(user: User)

    @Query("select * from `RegisterUser:` order by name asc")
    fun selectAllUser(): MutableLiveData<User>
}