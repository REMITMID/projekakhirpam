package com.example.projekakhirpam.presentation.dosen

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekakhirpam.presentation.theme.AksaPalette

/**
 * Komponen Avatar Dosen yang mendukung foto profil hasil upload galeri
 * serta fallback inisial nama dengan palet warna AKSA Yellow.
 */
@Composable
fun DosenAvatar(
    nama: String,
    fotoUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    fontSize: TextUnit = 18.sp,
    shape: Shape = CircleShape
) {
    val context = LocalContext.current

    // Coba decode foto dari URI jika tersedia
    val bitmap = remember(fotoUrl) {
        if (!fotoUrl.isNullOrBlank()) {
            try {
                val uri = Uri.parse(fotoUrl)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = android.graphics.ImageDecoder.createSource(context.contentResolver, uri)
                    android.graphics.ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.allocator = android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = false
                    }.asImageBitmap()
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri).asImageBitmap()
                }
            } catch (e: Exception) {
                // Jika URI berbasis file path atau stream biasa
                try {
                    val uri = Uri.parse(fotoUrl)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val decoded = BitmapFactory.decodeStream(inputStream)
                    decoded?.asImageBitmap()
                } catch (ex: Exception) {
                    null
                }
            }
        } else {
            null
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = "Foto $nama",
            modifier = modifier
                .size(size)
                .clip(shape),
            contentScale = ContentScale.Crop
        )
    } else {
        // Fallback: Inisial nama dengan styling khas AKSA
        val initial = remember(nama) {
            nama
                .replace(Regex("^(Dr\\.|Prof\\.|Ir\\.|Drs\\.|Dra\\.)\\s*", RegexOption.IGNORE_CASE), "")
                .trim()
                .split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .map { it.first() }
                .joinToString("")
                .uppercase()
                .ifBlank { "DS" }
        }

        Box(
            modifier = modifier
                .size(size)
                .clip(shape)
                .background(AksaPalette.AksaYellow),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                color = AksaPalette.DeepBlack
            )
        }
    }
}
