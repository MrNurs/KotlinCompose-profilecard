package com.example.profile.data.local

import com.example.profile.User
import com.example.profile.data.remote.ApiUserDto

fun UserEntity.toDomain(): User =
    User(
        id = id,
        name = name,
        bio = bio,
        followers = mutableListOf()
    )

fun User.toEntity(): UserEntity =
    UserEntity(
        id = id,
        name = name,
        bio = bio
    )
fun ApiUserDto.toEntity(): UserEntity =
    UserEntity(
        id = id.toString(),
        name = name,
        bio = email
    )
