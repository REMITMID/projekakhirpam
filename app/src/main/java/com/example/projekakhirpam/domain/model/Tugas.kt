package com.example.projekakhirpam.domain.model

data class Tugas(
    val id: String? = null,
    val userId: String? = null,
    val judul: String,
    val mataKuliah: String,
    val deskripsi: String,
    val deadline: String,   // format yyyy-MM-dd, contoh 2026-11-20
    val prioritas: String,  // High, Medium, Low
    val status: String      // To Do, In Progress, Completed
)

object TugasConst {
    const val PRIORITAS_HIGH = "High"
    const val PRIORITAS_MEDIUM = "Medium"
    const val PRIORITAS_LOW = "Low"

    const val STATUS_TODO = "To Do"
    const val STATUS_IN_PROGRESS = "In Progress"
    const val STATUS_COMPLETED = "Completed"

    val DAFTAR_PRIORITAS = listOf(PRIORITAS_HIGH, PRIORITAS_MEDIUM, PRIORITAS_LOW)
    val DAFTAR_STATUS = listOf(STATUS_TODO, STATUS_IN_PROGRESS, STATUS_COMPLETED)
}
