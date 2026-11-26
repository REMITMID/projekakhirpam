package com.example.projekakhirpam.domain.model

data class Jadwal(
    val id: String = "",
    val userId: String = "",
    val namaMatkul: String = "",
    val hari: Hari = Hari.SENIN,
    val jamMulai: String = "",
    val jamSelesai: String = "",
    val ruangan: String = "",
    val dosen: String = ""
)