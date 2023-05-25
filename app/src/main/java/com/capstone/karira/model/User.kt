package com.capstone.karira.model

data class User (
    val token: String,
    val email: String,
    val role: String,
    val skills: String
)

enum class Role {
    CLIENT, OWNER
}
