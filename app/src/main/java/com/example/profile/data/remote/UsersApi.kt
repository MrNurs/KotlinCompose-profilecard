package com.example.profile.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): ApiUserDto
}
