package com.example.projekakhirpam.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.R

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onBack: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val authState by viewModel.authState.collectAsState()
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) onLoginSuccess()
    }

    AksaAuthLayout(
        topActionLabel = "Daftar",
        onTopAction = onNavigateToSignUp,
        onBack = onBack,
        illustrationRes = R.drawable.auth_illustration_exam,
        illustrationDescription = "Ilustrasi mahasiswa sedang belajar untuk halaman masuk"
    ) {
        Text("Selamat datang kembali", style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
        Text(
            "Masukkan email dan kata sandi untuk melanjutkan.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant
        )

        Spacer(Modifier.height(2.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = aksaAuthTextFieldColors(),
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Kata sandi") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.heightIn(min = 48.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (passwordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                        tint = colors.onSurfaceVariant
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = aksaAuthTextFieldColors(),
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
        )

        if (authState is AuthState.Error) {
            Text(
                (authState as AuthState.Error).message,
                color = colors.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { viewModel.login(email.trim(), password) },
            enabled = authState !is AuthState.Loading,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.onSurface,
                contentColor = colors.surface,
                disabledContainerColor = colors.onSurfaceVariant,
                disabledContentColor = colors.surface
            ),
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = colors.surface, strokeWidth = 2.dp)
            } else {
                Text("Masuk", fontWeight = FontWeight.SemiBold)
            }
        }

    }
}
