package com.example.projekakhirpam.domain.model

data class MataKuliahCatatan(
        val id: String,
        val semester: Int,
        val nama: String
)

data class PertemuanCatatan(
        val id: String,
        val mataKuliahId: String,
        val nomor: Int
)

data class CatatanAkademik(
        val id: String,
        val pertemuanId: String,
        val judul: String,
        val isi: String,
        val createdAt: String = ""
)