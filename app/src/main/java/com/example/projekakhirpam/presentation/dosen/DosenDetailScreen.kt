package com.example.projekakhirpam.presentation.dosen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekakhirpam.domain.model.Dosen
import com.example.projekakhirpam.presentation.theme.AksaPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenDetailScreen(
    dosenId: String,
    viewModel: DosenViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val dosen = uiState.daftar.find { it.id == dosenId } ?: uiState.selectedDosen

    LaunchedEffect(dosenId) {
        if (dosen == null) {
            viewModel.loadDosenDetail(dosenId)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Detail Dosen", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    if (dosen != null) {
                        IconButton(onClick = { onNavigateToEdit(dosen.id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Dosen",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Dosen",
                                tint = AksaPalette.Error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        if (dosen == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = AksaPalette.AksaYellow)
                } else {
                    Text(
                        "Dosen tidak ditemukan",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Profil Dosen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DosenAvatar(
                            nama = dosen.nama,
                            fotoUrl = dosen.fotoUrl,
                            size = 88.dp,
                            fontSize = 32.sp,
                            shape = RoundedCornerShape(24.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = dosen.nama,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = AksaPalette.SoftGray
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = AksaPalette.AksaDarkGray
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (dosen.mataKuliah.isNotBlank()) dosen.mataKuliah else "Mata Kuliah Belum Diatur",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = AksaPalette.AksaDarkGray
                                )
                            }
                        }
                    }
                }

                // Tombol Aksi Cepat Kontak: Diselaraskan dengan tema warna di halaman list
                val whatsappGreen = Color(0xFF25D366)
                val telegramBlue = Color(0xFF24A1DE)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Tombol WhatsApp: Warna hijau logo WhatsApp dengan icon & teks putih
                    Button(
                        onClick = { bukaWhatsApp(context, dosen.noWa) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = whatsappGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WhatsApp",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Tombol Email: Menggunakan aksen warna kuning AKSA
                    Button(
                        onClick = { kirimEmail(context, dosen.email, dosen.nama) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AksaPalette.AksaYellow,
                            contentColor = AksaPalette.DeepBlack
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = AksaPalette.DeepBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Email",
                            fontWeight = FontWeight.Bold,
                            color = AksaPalette.DeepBlack
                        )
                    }

                    // Tombol Tambahan Telegram jika preferensi cara dihubungi adalah Telegram
                    if (dosen.kontakPreferensi.equals("Telegram", ignoreCase = true)) {
                        Button(
                            onClick = { bukaTelegram(context, dosen.noWa) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = telegramBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Telegram",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Rincian Kontak & Preferensi
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Informasi Kontak & Komunikasi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        DetailItem(
                            icon = Icons.AutoMirrored.Filled.Chat,
                            label = "Preferensi Cara Dihubungi",
                            value = dosen.kontakPreferensi.ifBlank { "WhatsApp" }
                        )

                        DetailItem(
                            icon = Icons.AutoMirrored.Filled.Chat,
                            label = "Nomor WhatsApp / Kontak",
                            value = dosen.noWa.ifBlank { "Belum diisi" }
                        )

                        DetailItem(
                            icon = Icons.Default.Email,
                            label = "Email Akademik",
                            value = dosen.email.ifBlank { "Belum diisi" }
                        )
                    }
                }

                // Tag Karakter Pengajaran Dosen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = AksaPalette.AksaDarkGray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Karakter Pengajaran",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (dosen.karakter.isEmpty()) {
                            Text(
                                "Belum ada tag karakter pengajaran.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                dosen.karakter.forEach { tag ->
                                    SuggestionChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                text = tag,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = AksaPalette.SoftGray,
                                            labelColor = AksaPalette.AksaDarkGray
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Catatan Bebas & Tips Mahasiswa
                if (dosen.catatan.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Catatan Bebas & Tips Mahasiswa",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dosen.catatan,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Dialog Konfirmasi Hapus
    if (showDeleteConfirmDialog && dosen != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Hapus Data Dosen?") },
            text = { Text("Apakah Anda yakin ingin menghapus \"${dosen.nama}\" dari direktori dosen?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        viewModel.deleteDosen(dosen.id) {
                            Toast.makeText(context, "Dosen berhasil dihapus", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AksaPalette.Error)
                ) {
                    Text("Hapus", color = AksaPalette.PureWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun bukaWhatsApp(context: Context, nomor: String) {
    if (nomor.isBlank()) {
        Toast.makeText(context, "Nomor WhatsApp belum diisi untuk dosen ini", Toast.LENGTH_SHORT).show()
        return
    }
    val cleanNomor = nomor.replace(Regex("[^0-9]"), "").let {
        if (it.startsWith("0")) "62" + it.substring(1) else it
    }
    val url = "https://wa.me/$cleanNomor"
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak dapat membuka aplikasi WhatsApp", Toast.LENGTH_SHORT).show()
    }
}

private fun bukaTelegram(context: Context, nomor: String) {
    if (nomor.isBlank()) {
        Toast.makeText(context, "Nomor kontak / username Telegram belum diisi", Toast.LENGTH_SHORT).show()
        return
    }
    val cleanNomor = nomor.replace(Regex("[^0-9]"), "").let {
        if (it.startsWith("0")) "62" + it.substring(1) else it
    }
    val url = "https://t.me/+$cleanNomor"
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak dapat membuka Telegram", Toast.LENGTH_SHORT).show()
    }
}

private fun kirimEmail(context: Context, email: String, namaDosen: String) {
    if (email.isBlank()) {
        Toast.makeText(context, "Alamat email belum diisi untuk dosen ini", Toast.LENGTH_SHORT).show()
        return
    }
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "Pertanyaan Akademik - Kepada $namaDosen")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak dapat membuka aplikasi email", Toast.LENGTH_SHORT).show()
    }
}

