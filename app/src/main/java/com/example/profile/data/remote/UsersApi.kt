package com.example.profile.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {
    @GET("users/{id}")
    suspend fun user(@Path("id") id: Int): ApiUser

    companion object {
        fun create(): UsersApi {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(retrofit2.converter.moshi.MoshiConverterFactory.create())
                .build()
            return retrofit.create(UsersApi::class.java)
        }
    }
}
