package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.data.model.DosenDto
import com.example.projekakhirpam.data.model.toDomain
import com.example.projekakhirpam.data.model.toDto
import com.example.projekakhirpam.domain.model.Dosen
import com.example.projekakhirpam.domain.repository.DosenRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

/**
 * Implementasi DosenRepository menggunakan Supabase Postgrest.
 * Data diisolasi secara ketat per user_id sehingga hanya bisa diakses oleh akun pembuatnya.
 */
class DosenRepositoryImpl(
    private val supabase: SupabaseClient
) : DosenRepository {

    private val _dosenList = MutableStateFlow<List<Dosen>>(emptyList())

    private fun getUserId(): String {
        return supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Pengguna belum login. Sesi tidak ditemukan.")
    }

    private suspend fun fetchRemote() {
        val userId = getUserId()
        val dtoList = supabase.postgrest["dosen"]
            .select {
                filter { eq("user_id", userId) }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<DosenDto>()
        _dosenList.value = dtoList.map { it.toDomain() }
    }

    override fun getDosenList(): Flow<List<Dosen>> = flow {
        emit(_dosenList.value)
        try {
            fetchRemote()
        } catch (_: Exception) {
            // Abaikan jika belum login atau offline saat initial flow emit
        }
        _dosenList.collect { emit(it) }
    }

    suspend fun refresh(): Result<Unit> = runCatching {
        fetchRemote()
    }

    override suspend fun getDosenById(id: String): Dosen? = runCatching {
        _dosenList.value.find { it.id == id } ?: run {
            val userId = getUserId()
            supabase.postgrest["dosen"]
                .select {
                    filter {
                        eq("id", id)
                        eq("user_id", userId)
                    }
                }
                .decodeSingleOrNull<DosenDto>()?.toDomain()
        }
    }.getOrNull()

    override suspend fun insertDosen(dosen: Dosen): Result<Unit> = runCatching {
        val userId = getUserId()
        val dto = dosen.toDto(userId)
        supabase.postgrest["dosen"].insert(dto)
        fetchRemote()
    }

    override suspend fun updateDosen(id: String, dosen: Dosen): Result<Unit> = runCatching {
        val userId = getUserId()
        val dto = dosen.toDto(userId)
        supabase.postgrest["dosen"].update(dto) {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }
        fetchRemote()
    }

    override suspend fun deleteDosen(id: String): Result<Unit> = runCatching {
        val userId = getUserId()
        supabase.postgrest["dosen"].delete {
            filter {
                eq("id", id)
                eq("user_id", userId)
            }
        }
        fetchRemote()
    }
}
