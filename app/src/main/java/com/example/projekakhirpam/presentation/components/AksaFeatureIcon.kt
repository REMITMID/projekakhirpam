package com.example.projekakhirpam.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AksaFeatureIcon(
    route: String,
    modifier: Modifier = Modifier,
    tint: Color
) {
    val image = when (route) {
        "tugas_list" -> Icons.Default.Assignment
        "jadwal_list" -> Icons.Default.CalendarMonth
        "catatan_list" -> Icons.Default.EditNote
        "dosen_list" -> Icons.Default.ContactPage
        "referensi_list" -> Icons.Default.MenuBook
        "aktivitas_list" -> Icons.Default.EventAvailable
        else -> Icons.Default.MenuBook
    }
    Icon(imageVector = image, contentDescription = null, tint = tint, modifier = modifier)
}
