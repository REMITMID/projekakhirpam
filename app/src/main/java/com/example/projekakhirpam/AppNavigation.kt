package com.example.projekakhirpam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projekakhirpam.domain.repository.AuthRepository
import com.example.projekakhirpam.presentation.aktivitas.*
import com.example.projekakhirpam.presentation.auth.*
import com.example.projekakhirpam.presentation.home.HomeScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiDetailScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiFormScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiListScreen
import com.example.projekakhirpam.presentation.koleksireferensi.ReferensiViewModel
import com.example.projekakhirpam.presentation.profile.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    aktivitasViewModel: AktivitasViewModel,
    referensiViewModel: ReferensiViewModel,
    authRepository: AuthRepository
) {
    var isLoading by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        val currentUser = authRepository.getCurrentUser()
        // Jika sudah login, langsung buka HALAMAN UTAMA (home)
        startDestination = if (currentUser != null) "home" else "login"
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(navController = navController, startDestination = startDestination) {

            // --- AUTH & PROFILE ---
            composable("login") {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        aktivitasViewModel.loadAktivitas()
                        referensiViewModel.loadReferensi()
                        navController.navigate("home") {
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
                        referensiViewModel.loadReferensi()
                        navController.navigate("home") {
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

            // --- HALAMAN UTAMA (DASHBOARD 6 MENU) ---
            composable("home") {
                HomeScreen(
                    onNavigateToFeature = { route -> navController.navigate(route) },
                    onProfileClick = { navController.navigate("profile") }
                )
            }

            // --- FITUR 5: KOLEKSI REFERENSI (FITURMU) ---
            composable("referensi_list") {
                ReferensiListScreen(
                    viewModel = referensiViewModel,
                    onNavigateToDetail = { referensiId ->
                        navController.navigate("referensi_detail/$referensiId")
                    },
                    onNavigateToAdd = {
                        navController.navigate("referensi_form")
                    }
                )
            }
            composable("referensi_detail/{id}") { backStackEntry ->
                val idString = backStackEntry.arguments?.getString("id")
                val referensiId = idString?.toIntOrNull() ?: 0

                ReferensiDetailScreen(
                    referensiId = referensiId,
                    viewModel = referensiViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { editId ->
                        navController.navigate("referensi_form?id=$editId")
                    }
                )
            }
            composable("referensi_form?id={id}") { backStackEntry ->
                val idString = backStackEntry.arguments?.getString("id")
                val referensiId = idString?.toIntOrNull()

                ReferensiFormScreen(
                    referensiId = referensiId,
                    viewModel = referensiViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // --- FITUR 6: AKTIVITAS KAMPUS (FITUR SEBELUMNYA) ---
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

            // --- PLACEHOLDER FITUR TEMAN KELOMPOK LAIN (FITUR 1-4) ---
            composable("tugas_list") { PlaceholderScreen("Manajemen Tugas Kuliah") }
            composable("jadwal_list") { PlaceholderScreen("Jadwal Perkuliahan") }
            composable("catatan_list") { PlaceholderScreen("Catatan Akademik") }
            composable("dosen_list") { PlaceholderScreen("Direktori Dosen") }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Halaman $title sedang dikembangkan oleh anggota kelompok.")
    }
}
