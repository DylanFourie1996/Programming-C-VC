package com.example.coinquest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserModel>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserModel?

    @Insert
    suspend fun insertUser(user: UserModel): Long
}
