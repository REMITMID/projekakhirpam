package com.example.projekakhirpam.presentation.dosen

import com.example.projekakhirpam.domain.model.Dosen

/**
 * Satu UI State komprehensif untuk fitur Direktori Dosen.
 * Sesuai panduan Modul MVVM.
 */
data class DosenUiState(
    val daftar: List<Dosen> = emptyList(),
    val selectedDosen: Dosen? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

