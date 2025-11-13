package com.example.profile.data.local

import com.example.profile.User

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
