package com.example.projekakhirpam

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.presentation.dosen.DosenDetailScreen
import com.example.projekakhirpam.presentation.dosen.DosenFormScreen
import com.example.projekakhirpam.presentation.dosen.DosenListScreen
import com.example.projekakhirpam.presentation.dosen.DosenViewModel
import com.example.projekakhirpam.domain.repository.AuthRepository
import com.example.projekakhirpam.presentation.aktivitas.*
import com.example.projekakhirpam.presentation.auth.*
import com.example.projekakhirpam.presentation.profile.ProfileScreen
import kotlinx.coroutines.runBlocking

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    aktivitasViewModel: AktivitasViewModel,
    referensiViewModel: ReferensiViewModel,
    authRepository: AuthRepository,
    catatanRepository: CatatanRepository,
    dosenViewModel: DosenViewModel = viewModel()
) {
    val currentUser = runBlocking { authRepository.getCurrentUser() }
    val startDestination = if (currentUser != null) "aktivitas_list" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate("aktivitas_list") { popUpTo("login") { inclusive = true } } },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = { navController.navigate("aktivitas_list") { popUpTo("signup") { inclusive = true } } },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("profile") {
            ProfileScreen(
                authRepository = authRepository,
                onLogout = { navController.navigate("login") { popUpTo(0) } },
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
                        onDeleteClick = { aktivitasViewModel.deleteAktivitas(it.id!!) { navController.popBackStack() } },
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
                        onSubmit = { updatedItem -> aktivitasViewModel.updateAktivitas(id!!, updatedItem) { navController.popBackStack() } },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            // --- PLACEHOLDER FITUR TEMAN KELOMPOK LAIN (FITUR 1-4) ---
            composable("tugas_list") { PlaceholderScreen("Manajemen Tugas Kuliah") }
            composable("jadwal_list") { PlaceholderScreen("Jadwal Perkuliahan") }
            // --- FITUR 4: DIREKTORI DOSEN ---
            composable("dosen_list") {
                DosenListScreen(
                    viewModel = dosenViewModel,
                    onNavigateToDetail = { dosenId ->
                        navController.navigate("dosen_detail/$dosenId")
                    },
                    onNavigateToAdd = {
                        navController.navigate("dosen_form")
                    },
                    onProfileClick = { navigateToMain("profile") },
                    onNavigateRoute = { route -> navigateToMain(route) }
                )
            }
            composable("dosen_detail/{id}") { backStackEntry ->
                val dosenId = backStackEntry.arguments?.getString("id").orEmpty()
                DosenDetailScreen(
                    dosenId = dosenId,
                    viewModel = dosenViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { editId ->
                        navController.navigate("dosen_form?id=$editId")
                    }
                )
            }
            composable("dosen_form?id={id}") { backStackEntry ->
                val dosenId = backStackEntry.arguments?.getString("id")
                DosenFormScreen(
                    dosenId = dosenId,
                    viewModel = dosenViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("dosen_form") {
                DosenFormScreen(
                    dosenId = null,
                    viewModel = dosenViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
            if (showMainNavigation) {
                AksaBottomNavigation(
                    selectedRoute = when (currentRoute) {
                        "home" -> "home"
                        "profile" -> "profile"
                        else -> "more"
                    },
                    onNavigate = { route ->
                        when (route) {
                            "home", "profile", "more" -> navigateToMain(route)
                            else -> {
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}