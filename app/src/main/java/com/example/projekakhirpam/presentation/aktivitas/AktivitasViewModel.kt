package com.example.projekakhirpam.presentation.aktivitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.domain.model.AktivitasKampus
import com.example.projekakhirpam.domain.repository.AktivitasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AktivitasUiState {
    object Loading : AktivitasUiState()
    data class Success(val list: List<AktivitasKampus>) : AktivitasUiState()
    data class Error(val message: String) : AktivitasUiState()
}

class AktivitasViewModel(private val repository: AktivitasRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AktivitasUiState>(AktivitasUiState.Loading)
    val uiState: StateFlow<AktivitasUiState> = _uiState

    init { loadAktivitas() }

    fun loadAktivitas() {
        viewModelScope.launch {
            _uiState.value = AktivitasUiState.Loading
            repository.getAktivitasList()
                .onSuccess { _uiState.value = AktivitasUiState.Success(it) }
                .onFailure { _uiState.value = AktivitasUiState.Error(it.message ?: "gagal memuat data") }
        }
    }

    fun addAktivitas(item: AktivitasKampus, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.insertAktivitas(item)
                .onSuccess { loadAktivitas(); onSuccess() }
                .onFailure { _uiState.value = AktivitasUiState.Error(it.message ?: "gagal menambah data") }
        }
    }

    fun updateAktivitas(id: String, item: AktivitasKampus, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateAktivitas(id, item)
                .onSuccess { loadAktivitas(); onSuccess() }
                .onFailure { _uiState.value = AktivitasUiState.Error(it.message ?: "gagal memperbarui data") }
        }
    }

    fun deleteAktivitas(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteAktivitas(id)
                .onSuccess { loadAktivitas(); onSuccess() }
                .onFailure { _uiState.value = AktivitasUiState.Error(it.message ?: "gagal menghapus data") }
        }
    }
}