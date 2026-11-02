package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Referensi
import com.example.projekakhirpam.presentation.components.AksaBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferensiListScreen(
    viewModel: ReferensiViewModel,
    onNavigateRoute: (String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAdd: () -> Unit
) {
    val referensiList by viewModel.referensiList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Koleksi Referensi") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Referensi")
            }
        },
        bottomBar = { AksaBottomNavigation("referensi_list", onNavigateRoute) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(
                    text = error ?: "Gagal memuat referensi",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                referensiList.isEmpty() -> Text(
                    text = "Belum ada referensi. Tekan + untuk menambahkan.",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(referensiList) { referensi ->
                        ReferensiCard(referensi = referensi, onClick = { onNavigateToDetail(referensi.id) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ReferensiCard(referensi: Referensi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = referensi.judul, style = MaterialTheme.typography.titleMedium)
            Text(text = "Penulis: ${referensi.penulis}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Kategori: ${referensi.kategori}", style = MaterialTheme.typography.labelMedium)
        }
    }
}
