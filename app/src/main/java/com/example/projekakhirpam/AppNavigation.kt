package com.example.projekakhirpam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projekakhirpam.domain.repository.AuthRepository
import com.example.projekakhirpam.presentation.aktivitas.*
import com.example.projekakhirpam.presentation.auth.*
import com.example.projekakhirpam.presentation.profile.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    aktivitasViewModel: AktivitasViewModel,
    authRepository: AuthRepository
) {
    var isLoading by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        val currentUser = authRepository.getCurrentUser()
        startDestination = if (currentUser != null) "aktivitas_list" else "login"
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(navController = navController, startDestination = startDestination) {
            composable("login") {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        aktivitasViewModel.loadAktivitas()
                        navController.navigate("aktivitas_list") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }
            composable("signup") {
                SignUpScreen(
                    viewModel = authViewModel,
                    onSignUpSuccess = {
                        aktivitasViewModel.loadAktivitas()
                        navController.navigate("aktivitas_list") {
                            popUpTo("signup") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }
            composable("profile") {
                ProfileScreen(
                    authRepository = authRepository,
                    onLogout = {
                        authViewModel.resetAuthState()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("aktivitas_list") {
                AktivitasListScreen(
                    viewModel = aktivitasViewModel,
                    onAddClick = { navController.navigate("aktivitas_form") },
                    onItemClick = { item -> navController.navigate("aktivitas_detail/${item.id}") },
                    onProfileClick = { navController.navigate("profile") }
                )
            }
            composable("aktivitas_form") {
                AktivitasFormScreen(
                    onSubmit = { item -> aktivitasViewModel.addAktivitas(item) { navController.popBackStack() } },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("aktivitas_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val state by aktivitasViewModel.uiState.collectAsState()
                if (state is AktivitasUiState.Success) {
                    val item = (state as AktivitasUiState.Success).list.find { it.id == id }
                    item?.let {
                        AktivitasDetailScreen(
                            item = it,
                            onEditClick = { navController.navigate("aktivitas_edit/${it.id}") },
                            onDeleteClick = {
                                it.id?.let { itemId ->
                                    aktivitasViewModel.deleteAktivitas(itemId) { navController.popBackStack() }
                                }
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
            composable("aktivitas_edit/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val state by aktivitasViewModel.uiState.collectAsState()
                if (state is AktivitasUiState.Success) {
                    val item = (state as AktivitasUiState.Success).list.find { it.id == id }
                    item?.let {
                        AktivitasFormScreen(
                            initialData = it,
                            onSubmit = { updatedItem ->
                                id?.let { itemId ->
                                    aktivitasViewModel.updateAktivitas(itemId, updatedItem) { navController.popBackStack() }
                                }
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}