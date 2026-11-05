package com.example.projekakhirpam.presentation.dosen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekakhirpam.domain.model.Dosen
import com.example.projekakhirpam.presentation.theme.AksaPalette

/**
 * Screen: Menghubungkan ViewModel dengan Content (State Hoisting).
 * Menampilkan daftar dosen dengan format 1 kartu per baris dan tombol pintasan WA + Email.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenListScreen(
    viewModel: DosenViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToAdd: () -> Unit,
    onProfileClick: () -> Unit,
    onNavigateRoute: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DosenListContent(
        state = uiState,
        query = viewModel.query,
        onQueryChange = viewModel::onQueryChange,
        onClearQuery = viewModel::clearQuery,
        onDosenClick = { dosen -> onNavigateToDetail(dosen.id) },
        onAddClick = onNavigateToAdd,
        onProfileClick = onProfileClick,
        onDismissError = viewModel::dismissError
    )
}

/**
 * Content: Stateless Composable yang menerima nilai dan event lambda.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenListContent(
    state: DosenUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onDosenClick: (Dosen) -> Unit,
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Direktori Dosen",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Buka Profil",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = AksaPalette.AksaYellow,
                contentColor = AksaPalette.DeepBlack,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Dosen"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Kolom Pencarian
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = {
                    Text(
                        "Cari nama, matkul, atau tag...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = onClearQuery) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus teks"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AksaPalette.AksaYellow,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // Pesan Error jika ada
            state.errorMessage?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onDismissError) {
                            Text("Tutup", color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Ringkasan Jumlah Dosen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Dosen Pengampu (${state.daftar.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (query.isNotBlank()) {
                    Text(
                        text = "Filter: \"$query\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Konten: 1 Kartu per Baris menggunakan LazyColumn
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AksaPalette.AksaYellow)
                    }
                }

                state.daftar.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.School,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Text(
                                text = if (query.isNotBlank()) "Tidak ada dosen yang cocok dengan pencarian" else "Belum ada data dosen tersimpan",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.daftar, key = { it.id }) { dosen ->
                            DosenRowCard(
                                dosen = dosen,
                                onClick = { onDosenClick(dosen) },
                                onChatWhatsApp = { hubungiWhatsApp(context, dosen.noWa) },
                                onSendEmail = { hubungiEmail(context, dosen.email, dosen.nama) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Kartu Dosen 1 baris 1 kartu (Horizontal Layout).
 * Menampilkan: Foto/Avatar, Nama Dosen, Mata Kuliah, Tag Karakter, serta Tombol Pintasan WA & Email.
 */
@Composable
fun DosenRowCard(
    dosen: Dosen,
    onClick: () -> Unit,
    onChatWhatsApp: () -> Unit,
    onSendEmail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto atau Avatar Dosen
                DosenAvatar(
                    nama = dosen.nama,
                    fotoUrl = dosen.fotoUrl,
                    size = 56.dp,
                    fontSize = 18.sp,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                // Detail Teks (Nama, Matkul)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dosen.nama,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (dosen.mataKuliah.isNotBlank()) dosen.mataKuliah else "Mata kuliah belum diatur",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (dosen.mataKuliah.isNotBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Tag Karakter Pengajaran (Maksimal 3 chip)
            if (dosen.karakter.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    dosen.karakter.take(3).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AksaPalette.SoftGray,
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = AksaPalette.AksaDarkGray,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
            Spacer(modifier = Modifier.height(8.dp))

            // Baris Pintasan Cepat: HANYA WhatsApp dan Email
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Info preferensi kontak badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AksaPalette.AksaYellow.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Preferensi: ${dosen.kontakPreferensi}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AksaPalette.DeepBlack,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // 2 Tombol Pintasan Cepat
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Tombol WhatsApp: Hijau logo WA dengan icon & text putih
                    val whatsappGreen = Color(0xFF25D366)
                    Button(
                        onClick = onChatWhatsApp,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = whatsappGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WhatsApp",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Tombol Email: Menggunakan aksen kuning AKSA
                    OutlinedButton(
                        onClick = onSendEmail,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        border = BorderStroke(1.dp, AksaPalette.AksaYellow),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AksaPalette.AksaYellow.copy(alpha = 0.15f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = AksaPalette.DeepBlack,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Email",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AksaPalette.DeepBlack
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fungsi pintasan membuka WhatsApp dengan proteksi jika nomor kosong.
 */
private fun hubungiWhatsApp(context: Context, nomor: String) {
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

/**
 * Fungsi pintasan mengirim Email dengan proteksi jika email kosong.
 */
private fun hubungiEmail(context: Context, email: String, namaDosen: String) {
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

