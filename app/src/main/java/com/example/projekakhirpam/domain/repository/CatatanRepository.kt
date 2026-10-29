package com.example.projekakhirpam.domain.repository

import com.example.projekakhirpam.domain.model.*

interface CatatanRepository {

    // Mata kuliah berdasarkan semester
    suspend fun mataKuliah(
        semester: Int
    ): List<MataKuliahCatatan>

    suspend fun tambahMataKuliah(
        semester: Int,
        nama: String
    )

    suspend fun hapusMataKuliah(
        id: String
    )

    // Pertemuan berdasarkan mata kuliah
    suspend fun pertemuan(
        mataKuliahId: String
    ): List<PertemuanCatatan>

    suspend fun tambahPertemuan(
        mataKuliahId: String,
        nomor: Int
    )

    suspend fun hapusPertemuan(
        id: String
    )

    // Catatan berdasarkan pertemuan
    suspend fun catatan(
        pertemuanId: String
    ): List<CatatanAkademik>

    suspend fun simpanCatatan(
        pertemuanId: String,
        id: String?,
        judul: String,
        isi: String
    )

    suspend fun hapusCatatan(
        id: String
    )
}