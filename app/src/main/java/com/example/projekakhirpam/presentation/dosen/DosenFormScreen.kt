package com.example.projekakhirpam.presentation.dosen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekakhirpam.domain.model.Dosen
import com.example.projekakhirpam.presentation.theme.AksaPalette
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenFormScreen(
    dosenId: String? = null,
    viewModel: DosenViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = !dosenId.isNullOrBlank()
    val uiState by viewModel.uiState.collectAsState()

    // State Input Form
    var nama by remember { mutableStateOf("") }
    var mataKuliah by remember { mutableStateOf("") }
    var noWa by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var kontakPreferensi by remember { mutableStateOf("WhatsApp") }
    var fotoUrl by remember { mutableStateOf<String?>(null) }
    var catatan by remember { mutableStateOf("") }
    var tagList by remember { mutableStateOf(listOf<String>()) }
    var tagInput by remember { mutableStateOf("") }
    var tagError by remember { mutableStateOf<String?>(null) }

    // Launcher untuk memilih foto dari galeri HP
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            fotoUrl = uri.toString()
        }
    }

    // Jika mode edit, isi form dengan data yang sudah ada
    LaunchedEffect(dosenId) {
        if (isEditMode && dosenId != null) {
            val existing = uiState.daftar.find { it.id == dosenId } ?: uiState.selectedDosen
            if (existing != null) {
                nama = existing.nama
                mataKuliah = existing.mataKuliah
                noWa = existing.noWa
                email = existing.email
                kontakPreferensi = existing.kontakPreferensi
                fotoUrl = existing.fotoUrl
                catatan = existing.catatan
                tagList = existing.karakter
            } else {
                viewModel.loadDosenDetail(dosenId)
            }
        }
    }

    // Update form jika selectedDosen selesai dimuat
    LaunchedEffect(uiState.selectedDosen) {
        if (isEditMode && uiState.selectedDosen != null && nama.isBlank()) {
            val existing = uiState.selectedDosen!!
            nama = existing.nama
            mataKuliah = existing.mataKuliah
            noWa = existing.noWa
            email = existing.email
            kontakPreferensi = existing.kontakPreferensi
            fotoUrl = existing.fotoUrl
            catatan = existing.catatan
            tagList = existing.karakter
        }
    }

    fun handleTambahTag() {
        val cleanTag = tagInput.trim()
        when {
            cleanTag.isBlank() -> {
                tagError = "Tag tidak boleh kosong"
            }
            cleanTag.length > 20 -> {
                tagError = "Maksimal 20 karakter per tag"
            }
            tagList.size >= 3 -> {
                tagError = "Maksimal 3 tag karakter per dosen"
            }
            tagList.any { it.equals(cleanTag, ignoreCase = true) } -> {
                tagError = "Tag sudah ada"
            }
            else -> {
                tagList = tagList + cleanTag
                tagInput = ""
                tagError = null
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Data Dosen" else "Tambah Dosen",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bagian Upload & Pratinjau Foto Profil Dosen
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DosenAvatar(
                        nama = nama.ifBlank { "DS" },
                        fotoUrl = fotoUrl,
                        size = 80.dp,
                        fontSize = 28.sp,
                        shape = RoundedCornerShape(20.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AksaPalette.AksaYellow,
                                contentColor = AksaPalette.DeepBlack
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (fotoUrl == null) "Pilih Foto Profil" else "Ganti Foto",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (fotoUrl != null) {
                            OutlinedButton(
                                onClick = { fotoUrl = null },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Hapus foto",
                                    tint = AksaPalette.Error,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Hapus Foto", fontSize = 12.sp, color = AksaPalette.Error)
                            }
                        }
                    }
                }
            }

            // Bagian Informasi Utama Dosen
            Text(
                text = "Identitas Dosen",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Dosen *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = mataKuliah,
                onValueChange = { mataKuliah = it },
                label = { Text("Mata Kuliah Diampu") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Kontak & Preferensi
            Text(
                text = "Kontak Dosen",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = noWa,
                onValueChange = { noWa = it },
                label = { Text("Nomor WhatsApp") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Akademik") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Preferensi Cara Dihubungi (Ditambahkan opsi Telegram)
            Text(
                text = "Preferensi Cara Dihubungi",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("WhatsApp", "Telegram", "Email", "Tatap Muka").forEach { opsi ->
                    val isSelected = kontakPreferensi.equals(opsi, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { kontakPreferensi = opsi },
                        label = { Text(opsi, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AksaPalette.AksaYellow,
                            selectedLabelColor = AksaPalette.DeepBlack
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Tag Karakter Pengajaran Dosen (Maksimal 3 tag)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tag Karakter Dosen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${tagList.size}/3 tag",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = tagInput,
                    onValueChange = {
                        tagInput = it
                        if (tagError != null) tagError = null
                    },
                    label = { Text("Tag baru (maks 20 huruf)") },
                    singleLine = true,
                    enabled = tagList.size < 3,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { handleTambahTag() },
                    enabled = tagList.size < 3 && tagInput.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AksaPalette.AksaYellow,
                        contentColor = AksaPalette.DeepBlack
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Tag")
                }
            }

            tagError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Tampilan Chip Tag yang sudah ditambahkan
            if (tagList.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tagList.forEach { tag ->
                        InputChip(
                            selected = false,
                            onClick = {},
                            label = { Text(tag, fontSize = 12.sp) },
                            trailingIcon = {
                                IconButton(
                                    onClick = { tagList = tagList.filterNot { it == tag } },
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Hapus tag",
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            },
                            colors = InputChipDefaults.inputChipColors(
                                containerColor = AksaPalette.SoftGray,
                                labelColor = AksaPalette.AksaDarkGray
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Kolom Teks Bebas: Catatan Hal-hal Lain
            Text(
                text = "Catatan Bebas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = { Text("Catatan Bebas / Tips Mahasiswa") },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tombol Simpan
            Button(
                onClick = {
                    // Validasi: HANYA NAMA DOSEN YANG WAJIB!
                    if (nama.isBlank()) {
                        Toast.makeText(context, "Nama Dosen wajib diisi", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val finalDosen = Dosen(
                        id = if (isEditMode && dosenId != null) dosenId else UUID.randomUUID().toString(),
                        nama = nama.trim(),
                        fotoUrl = fotoUrl,
                        noWa = noWa.trim(),
                        email = email.trim(),
                        mataKuliah = mataKuliah.trim(),
                        kontakPreferensi = kontakPreferensi,
                        karakter = tagList,
                        catatan = catatan.trim()
                    )

                    if (isEditMode && dosenId != null) {
                        viewModel.updateDosen(dosenId, finalDosen) {
                            Toast.makeText(context, "Data dosen berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        }
                    } else {
                        viewModel.insertDosen(finalDosen) {
                            Toast.makeText(context, "Dosen baru berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        }
                    }
                },
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AksaPalette.AksaYellow,
                    contentColor = AksaPalette.DeepBlack
                )
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AksaPalette.DeepBlack,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (isEditMode) "Perbarui Data Dosen" else "Simpan Dosen",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

