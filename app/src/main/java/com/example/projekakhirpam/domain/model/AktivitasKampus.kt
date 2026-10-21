package com.example.projekakhirpam.domain.model

data class AktivitasKampus(
    val id: String? = null,
    val userId: String? = null,
    val judulKegiatan: String,
    val kategori: String,
    val tanggalPelaksanaan: String,
    val lokasi: String,
    val penyelenggara: String,
    val deskripsi: String,
    val statusPartisipasi: String
)