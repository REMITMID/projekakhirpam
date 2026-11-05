package com.example.projekakhirpam.data.model

import com.example.projekakhirpam.domain.model.Hari
import com.example.projekakhirpam.domain.model.Jadwal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JadwalDto(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("nama_matkul") val namaMatkul: String = "",
    @SerialName("hari") val hari: String = "SENIN",
    @SerialName("jam_mulai") val jamMulai: String = "",
    @SerialName("jam_selesai") val jamSelesai: String = "",
    @SerialName("ruangan") val ruangan: String = "",
    @SerialName("dosen") val dosen: String = ""
)

@Serializable
data class JadwalInsert(
    @SerialName("user_id") val userId: String,
    @SerialName("nama_matkul") val namaMatkul: String,
    @SerialName("hari") val hari: String,
    @SerialName("jam_mulai") val jamMulai: String,
    @SerialName("jam_selesai") val jamSelesai: String,
    @SerialName("ruangan") val ruangan: String,
    @SerialName("dosen") val dosen: String
)

@Serializable
data class JadwalUpdate(
    @SerialName("nama_matkul") val namaMatkul: String,
    @SerialName("hari") val hari: String,
    @SerialName("jam_mulai") val jamMulai: String,
    @SerialName("jam_selesai") val jamSelesai: String,
    @SerialName("ruangan") val ruangan: String,
    @SerialName("dosen") val dosen: String
)

fun JadwalDto.toDomain() = Jadwal(
    id = id.orEmpty(),
    userId = userId.orEmpty(),
    namaMatkul = namaMatkul,
    hari = Hari.fromString(hari),
    jamMulai = jamMulai,
    jamSelesai = jamSelesai,
    ruangan = ruangan,
    dosen = dosen
)

fun Jadwal.toInsert(userId: String) = JadwalInsert(
    userId = userId,
    namaMatkul = namaMatkul,
    hari = hari.name,
    jamMulai = jamMulai,
    jamSelesai = jamSelesai,
    ruangan = ruangan,
    dosen = dosen
)

fun Jadwal.toUpdate() = JadwalUpdate(
    namaMatkul = namaMatkul,
    hari = hari.name,
    jamMulai = jamMulai,
    jamSelesai = jamSelesai,
    ruangan = ruangan,
    dosen = dosen
)