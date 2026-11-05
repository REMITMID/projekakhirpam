package com.example.projekakhirpam.presentation.jadwal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.data.repository.JadwalRepositoryImp
import com.example.projekakhirpam.domain.model.Jadwal
import com.example.projekakhirpam.domain.repository.JadwalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface JadwalUiState {
    data object Loading : JadwalUiState
    data class Success(val list: List<Jadwal>) : JadwalUiState
    data class Error(val message: String) : JadwalUiState
}

class JadwalViewModel(
    private val repository: JadwalRepository = JadwalRepositoryImp()
) : ViewModel() {

    private val _uiState = MutableStateFlow<JadwalUiState>(JadwalUiState.Loading)
    val uiState: StateFlow<JadwalUiState> = _uiState.asStateFlow()

    init {
        loadJadwalList()
    }

    fun loadJadwalList() {
        viewModelScope.launch {
            _uiState.value = JadwalUiState.Loading
            repository.getJadwalList()
                .onSuccess { _uiState.value = JadwalUiState.Success(it) }
                .onFailure { _uiState.value = JadwalUiState.Error(it.message ?: "Gagal memuat jadwal") }
        }
    }

    fun addJadwal(item: Jadwal, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.insertJadwal(item)
                .onSuccess {
                    val list = repository.getJadwalList().getOrDefault(emptyList())
                    _uiState.value = JadwalUiState.Success(list)
                    onSuccess()
                }
                .onFailure { _uiState.value = JadwalUiState.Error(it.message ?: "Gagal menyimpan jadwal") }
        }
    }

    fun updateJadwal(id: String, item: Jadwal, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateJadwal(id, item)
                .onSuccess {
                    val list = repository.getJadwalList().getOrDefault(emptyList())
                    _uiState.value = JadwalUiState.Success(list)
                    onSuccess()
                }
                .onFailure { _uiState.value = JadwalUiState.Error(it.message ?: "Gagal memperbarui jadwal") }
        }
    }

    fun deleteJadwal(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteJadwal(id)
                .onSuccess {
                    val list = repository.getJadwalList().getOrDefault(emptyList())
                    _uiState.value = JadwalUiState.Success(list)
                    onSuccess()
                }
                .onFailure { _uiState.value = JadwalUiState.Error(it.message ?: "Gagal menghapus jadwal") }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return JadwalViewModel() as T
            }
        }
    }
}
