package com.example.profile

data class User(
    val id: String,
    val name: String,
    val bio: String,
    val followers: MutableList<String> = mutableListOf()
)
