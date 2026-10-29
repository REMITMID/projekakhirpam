package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.domain.model.Referensi
import com.example.projekakhirpam.domain.repository.ReferensiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReferensiViewModel(private val repository: ReferensiRepository) : ViewModel() {

    private val _referensiList = MutableStateFlow<List<Referensi>>(emptyList())
    val referensiList: StateFlow<List<Referensi>> = _referensiList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadReferensi()
    }

    fun loadReferensi() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getSemuaReferensi().collect { data ->
                    _referensiList.value = data
                }
            } catch (e: Exception) {
                // Tangani error di sini (misal: tampilkan Snackbar)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun hapusReferensi(id: Int) {
        viewModelScope.launch {
            repository.hapusReferensi(id)
            loadReferensi() // Refresh data setelah dihapus
        }
    }
}
