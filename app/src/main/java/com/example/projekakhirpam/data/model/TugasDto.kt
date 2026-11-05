package com.example.projekakhirpam.data.model

import com.example.projekakhirpam.domain.model.Tugas
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TugasDto(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("judul") val judul: String,
    @SerialName("mata_kuliah") val mataKuliah: String,
    @SerialName("deskripsi") val deskripsi: String,
    @SerialName("deadline") val deadline: String,
    @SerialName("prioritas") val prioritas: String,
    @SerialName("status") val status: String
)

fun TugasDto.toDomain() = Tugas(
    id = id,
    userId = userId,
    judul = judul,
    mataKuliah = mataKuliah,
    deskripsi = deskripsi,
    deadline = deadline,
    prioritas = prioritas,
    status = status
)

fun Tugas.toDto() = TugasDto(
    id = id,
    userId = userId,
    judul = judul,
    mataKuliah = mataKuliah,
    deskripsi = deskripsi,
    deadline = deadline,
    prioritas = prioritas,
    status = status
)
