package com.example.projekakhirpam.presentation.aktivitas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.AktivitasKampus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitasListScreen(
    viewModel: AktivitasViewModel,
    onAddClick: () -> Unit,
    onItemClick: (AktivitasKampus) -> Unit,
    onProfileClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("aktivitas kampus") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "profil")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "tambah")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val current = state) {
                is AktivitasUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is AktivitasUiState.Error -> Text(text = current.message, modifier = Modifier.align(Alignment.Center))
                is AktivitasUiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(current.list) { item ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable { onItemClick(item) }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = item.judulKegiatan, style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = item.kategori, style = MaterialTheme.typography.bodySmall)
                                    Text(text = item.statusPartisipasi, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}