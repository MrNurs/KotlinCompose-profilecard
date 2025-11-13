package com.example.profile.data.remote
import com.example.profile.User
data class ApiUser(
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)

fun ApiUser.toDomain(): User =
    User(
        id = if (id == 0) "me" else id.toString(),
        name = name,
        bio = "@$username • $email",
        followers = mutableListOf()
    )
