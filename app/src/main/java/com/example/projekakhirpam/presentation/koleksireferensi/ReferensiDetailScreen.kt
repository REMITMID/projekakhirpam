package com.example.projekakhirpam.presentation.koleksireferensi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.Referensi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferensiDetailScreen(
    referensiId: Int,
    viewModel: ReferensiViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    var referensi by remember { mutableStateOf<Referensi?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(referensiId) {
        viewModel.getReferensiById(referensiId) { item ->
            referensi = item
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Referensi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                referensi == null -> Text(
                    text = "Data referensi tidak ditemukan",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> {
                    val item = referensi!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = item.judul, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        HorizontalDivider()

                        DetailRow(label = "Penulis", value = item.penulis.ifBlank { "-" })
                        DetailRow(label = "Tahun Terbit", value = item.tahunTerbit.ifBlank { "-" })
                        DetailRow(label = "Kategori", value = item.kategori.ifBlank { "-" })
                        DetailRow(label = "URL Referensi", value = item.urlReferensi.ifBlank { "-" })

                        if (item.catatan.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Catatan:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = item.catatan,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f, fill = false))
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onNavigateToEdit(referensiId) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Edit")
                            }
                            Button(
                                onClick = {
                                    viewModel.hapusReferensi(referensiId)
                                    onNavigateBack()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Hapus")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
