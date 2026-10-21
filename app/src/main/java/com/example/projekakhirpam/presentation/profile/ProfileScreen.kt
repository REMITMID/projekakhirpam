package com.example.projekakhirpam.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.domain.model.User
import com.example.projekakhirpam.domain.repository.AuthRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authRepository: AuthRepository,
    onLogout: () -> Unit,
    onBackClick: () -> Unit
) {
    var user by remember { mutableStateOf<User?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        user = authRepository.getCurrentUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("profil pengguna") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("kembali") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("informasi akun", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("email: ${user?.email ?: "-"}")
                    Text("user ID: ${user?.id ?: "-"}")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    scope.launch {
                        authRepository.logout()
                        onLogout()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("keluar / logout")
            }
        }
    }
}