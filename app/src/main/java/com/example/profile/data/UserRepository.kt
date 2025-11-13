package com.example.profile.data

import com.example.profile.data.local.UserDao
import com.example.profile.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {

    fun observeUser(id: String): Flow<UserEntity?> =
        dao.observeUser(id)

    suspend fun getUser(id: String): UserEntity? =
        dao.getUser(id)

    suspend fun upsert(user: UserEntity) =
        dao.upsert(user)
}
