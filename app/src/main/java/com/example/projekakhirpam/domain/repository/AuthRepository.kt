package com.example.projekakhirpam.domain.repository

import com.example.projekakhirpam.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<User>
    suspend fun signUp(email: String, pass: String): Result<User>
    fun getCurrentUser(): User?
    suspend fun logout(): Result<Unit>
}