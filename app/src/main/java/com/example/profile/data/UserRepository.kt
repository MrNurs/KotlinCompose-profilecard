package com.example.profile.data

import com.example.profile.User
import com.example.profile.data.local.UserDao
import com.example.profile.data.local.toDomain
import com.example.profile.data.local.toEntity
import com.example.profile.data.local.toEntity
import com.example.profile.data.remote.UsersApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val api: UsersApi
) {

    fun observeUser(id: String): Flow<User?> =
        dao.observeUser(id).map { it?.toDomain() }

    suspend fun getUser(id: String): User? =
        dao.getUser(id)?.toDomain()

    suspend fun upsert(user: User) {
        dao.upsert(user.toEntity())
    }

    suspend fun refreshUser(id: String) {
        val intId = id.toIntOrNull() ?: return
        val remote = api.getUser(intId)
        dao.upsert(remote.toEntity())
    }
}
