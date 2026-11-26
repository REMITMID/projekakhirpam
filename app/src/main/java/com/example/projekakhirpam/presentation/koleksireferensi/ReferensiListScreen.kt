package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Referensi

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

    LaunchedEffect(Unit) {
        viewModel.loadReferensi()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Koleksi Referensi", fontWeight = FontWeight.Bold) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Referensi")
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = error ?: "Gagal memuat referensi",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.loadReferensi() }) {
                        Text("Coba Lagi")
                    }
                }
                referensiList.isEmpty() -> Text(
                    text = "Belum ada referensi. Tekan + untuk menambahkan.",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(referensiList, key = { it.id }) { referensi ->
                        ReferensiCard(referensi = referensi, onClick = { onNavigateToDetail(referensi.id) })
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = referensi.judul, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "Penulis: ${referensi.penulis}", style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (referensi.kategori.isNotBlank()) {
                    Text(text = "Kategori: ${referensi.kategori}", style = MaterialTheme.typography.labelMedium)
                }
                if (referensi.tahunTerbit.isNotBlank()) {
                    Text(text = "Tahun: ${referensi.tahunTerbit}", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
