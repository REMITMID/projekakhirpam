package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.data.model.AktivitasKampusDto
import com.example.projekakhirpam.data.model.toDomain
import com.example.projekakhirpam.data.model.toDto
import com.example.projekakhirpam.domain.model.AktivitasKampus
import com.example.projekakhirpam.domain.repository.AktivitasRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class AktivitasRepositoryImpl(private val supabase: SupabaseClient) : AktivitasRepository {
    override suspend fun getAktivitasList(): Result<List<AktivitasKampus>> = runCatching {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("user belum login")
        val response = supabase.postgrest["aktivitas_kampus"]
            .select { filter { eq("user_id", userId) } }
            .decodeList<AktivitasKampusDto>()
        response.map { it.toDomain() }
    }

    override suspend fun insertAktivitas(item: AktivitasKampus): Result<Unit> = runCatching {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("user belum login")
        val dto = item.copy(userId = userId).toDto()
        supabase.postgrest["aktivitas_kampus"].insert(dto)
    }

    override suspend fun updateAktivitas(id: String, item: AktivitasKampus): Result<Unit> = runCatching {
        supabase.postgrest["aktivitas_kampus"].update(item.toDto()) {
            filter { eq("id", id) }
        }
    }

    override suspend fun deleteAktivitas(id: String): Result<Unit> = runCatching {
        supabase.postgrest["aktivitas_kampus"].delete {
            filter { eq("id", id) }
        }
    }
}