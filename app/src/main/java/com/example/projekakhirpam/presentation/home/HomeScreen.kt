package com.example.projekakhirpam.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class FeatureMenuItem(
    val title: String,
    val description: String,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFeature: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    val menuItems = listOf(
        FeatureMenuItem("Tugas Kuliah", "Manajemen Tugas Kuliah", "tugas_list"),
        FeatureMenuItem("Jadwal Kuliah", "Jadwal Perkuliahan", "jadwal_list"),
        FeatureMenuItem("Catatan", "Catatan Akademik", "catatan_list"),
        FeatureMenuItem("Direktori Dosen", "Informasi & Kontak Dosen", "dosen_list"),
        FeatureMenuItem("Koleksi Referensi", "Jurnal, Buku, & Artikel", "referensi_list"),
        FeatureMenuItem("Aktivitas Kampus", "Kegiatan & Organisasi", "aktivitas_list")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AKSA - Dashboard") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profil")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Selamat Datang di AKSA",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Pilih menu fitur untuk memulai:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(menuItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToFeature(item.route) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
