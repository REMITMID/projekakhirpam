package com.example.projekakhirpam.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable

/** Shared field colors: strong neutral borders, with a dark-mode-safe foreground. */
@Composable
fun aksaOutlinedTextFieldColors(): TextFieldColors {
    val scheme = MaterialTheme.colorScheme
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = scheme.onSurface,
        unfocusedTextColor = scheme.onSurface,
        disabledTextColor = scheme.onSurfaceVariant,
        errorTextColor = scheme.onSurface,
        focusedContainerColor = scheme.surface,
        unfocusedContainerColor = scheme.surface,
        disabledContainerColor = scheme.surfaceVariant,
        errorContainerColor = scheme.surface,
        cursorColor = scheme.onSurface,
        errorCursorColor = scheme.error,
        focusedBorderColor = scheme.onSurface,
        unfocusedBorderColor = scheme.onSurface,
        disabledBorderColor = scheme.outline,
        errorBorderColor = scheme.error,
        focusedLabelColor = scheme.onSurface,
        unfocusedLabelColor = scheme.onSurfaceVariant,
        disabledLabelColor = scheme.onSurfaceVariant,
        errorLabelColor = scheme.error,
        focusedLeadingIconColor = scheme.onSurface,
        unfocusedLeadingIconColor = scheme.onSurfaceVariant,
        focusedTrailingIconColor = scheme.onSurface,
        unfocusedTrailingIconColor = scheme.onSurfaceVariant
    )
}
