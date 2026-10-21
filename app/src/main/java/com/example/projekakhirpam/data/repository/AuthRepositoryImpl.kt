package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.domain.model.User
import com.example.projekakhirpam.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepositoryImpl(private val supabase: SupabaseClient) : AuthRepository {
    override suspend fun login(email: String, pass: String): Result<User> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email = email
            password = pass
        }
        val user = supabase.auth.currentUserOrNull() ?: throw Exception("user tidak ditemukan")
        User(id = user.id, email = user.email ?: "")
    }

    override suspend fun signUp(email: String, pass: String): Result<User> = runCatching {
        supabase.auth.signUpWith(Email) {
            this.email = email
            password = pass
        }
        val user = supabase.auth.currentUserOrNull() ?: throw Exception("gagal mendaftar")
        User(id = user.id, email = user.email ?: "")
    }

    override fun getCurrentUser(): User? {
        val user = supabase.auth.currentUserOrNull()
        return user?.let { User(id = it.id, email = it.email ?: "") }
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        supabase.auth.signOut()
    }
}