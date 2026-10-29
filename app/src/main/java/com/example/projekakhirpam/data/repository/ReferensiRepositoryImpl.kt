package com.example.projekakhirpam.data.repository

import com.example.projekakhirpam.domain.model.Referensi
import com.example.projekakhirpam.domain.repository.ReferensiRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReferensiRepositoryImpl(private val supabase: SupabaseClient) : ReferensiRepository {

    private val table = supabase.postgrest["koleksi_referensi"]

    override suspend fun getSemuaReferensi(): Flow<List<Referensi>> = flow {
        val referensiList = table.select().decodeList<Referensi>()
        emit(referensiList)
    }

    override suspend fun getReferensiById(id: Int): Referensi? {
        return table.select { filter { eq("id", id) } }.decodeSingleOrNull<Referensi>()
    }

    override suspend fun tambahReferensi(referensi: Referensi) {
        table.insert(referensi)
    }

    override suspend fun updateReferensi(referensi: Referensi) {
        table.update(referensi) {
            filter { eq("id", referensi.id) }
        }
    }

    override suspend fun hapusReferensi(id: Int) {
        table.delete {
            filter { eq("id", id) }
        }
    }
}