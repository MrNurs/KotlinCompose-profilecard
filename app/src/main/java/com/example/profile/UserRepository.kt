package com.example.profile

object UserRepository {

    private val users = mutableMapOf(
        "me" to User(
            id = "me",
            name = "Dilmagambet Nurzhigit",
            bio = "Computer Science | BACKEND/ROBOTICS",
            followers = mutableListOf("peter", "gosling")
        ),
        "peter" to User(
            id = "peter",
            name = "Peter Parker",
            bio = "Friendly neighborhood dev",
            followers = mutableListOf("me")
        ),
        "gosling" to User(
            id = "gosling",
            name = "Ryan Gosling",
            bio = "Literally me.",
            followers = mutableListOf()
        )
    )

    fun getUser(id: String): User? = users[id]

    fun follow(targetId: String, followerId: String) {
        val target = users[targetId] ?: return
        if (!target.followers.contains(followerId)) {
            target.followers.add(followerId)
        }
    }

    fun unfollow(targetId: String, followerId: String) {
        users[targetId]?.followers?.remove(followerId)
    }
    fun updateUser(updated: User) {
        users[updated.id] = updated
    }
}
