    package com.example.projekakhirpam.presentation.tugas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Tugas
import com.example.projekakhirpam.domain.model.TugasConst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasListScreen(
    viewModel: TugasViewModel,
    onAddClick: () -> Unit,
    onItemClick: (Tugas) -> Unit,
    onProfileClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val filter by viewModel.filterStatus.collectAsState()

    PesanErrorToast(viewModel)

    // ViewModel bisa dibuat sebelum user login, jadi muat ulang kalau datanya belum berhasil dimuat
    LaunchedEffect(Unit) {
        if (viewModel.uiState.value !is TugasUiState.Success) viewModel.loadTugas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manajemen Tugas") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "profil")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "tambah tugas")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Filter status
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf(TugasViewModel.FILTER_SEMUA) + TugasConst.DAFTAR_STATUS) { pilihan ->
                    FilterChip(
                        selected = filter == pilihan,
                        onClick = { viewModel.setFilter(pilihan) },
                        label = { Text(pilihan) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (val current = state) {
                    is TugasUiState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                    is TugasUiState.Error -> Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(current.message)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadTugas() }) { Text("Coba lagi") }
                    }

                    is TugasUiState.Success -> {
                        val tampil = if (filter == TugasViewModel.FILTER_SEMUA) {
                            current.list
                        } else {
                            current.list.filter { it.status == filter }
                        }

                        if (tampil.isEmpty()) {
                            Text(
                                text = "Belum ada tugas. Tekan + untuk menambah.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            // Daftar tugas (sudah urut dari deadline terdekat)
                            LazyColumn(
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(tampil, key = { it.id ?: it.judul }) { tugas ->
                                    TugasCard(
                                        tugas = tugas,
                                        onClick = { onItemClick(tugas) },
                                        onToggle = { viewModel.toggleSelesai(tugas) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TugasCard(tugas: Tugas, onClick: () -> Unit, onToggle: () -> Unit) {
    val selesai = tugas.status == TugasConst.STATUS_COMPLETED
    val terlambat = isTerlambat(tugas.deadline, tugas.status)
    val (latarPrioritas, teksPrioritas) = warnaPrioritas(tugas.prioritas)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = selesai, onCheckedChange = { onToggle() })
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tugas.judul,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (selesai) TextDecoration.LineThrough else null
                )
                Text(
                    text = tugas.mataKuliah,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    LabelKecil(tugas.prioritas, latarPrioritas, teksPrioritas)
                    LabelKecil(
                        tugas.status,
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = labelDeadline(tugas.deadline, tugas.status),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (terlambat) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
