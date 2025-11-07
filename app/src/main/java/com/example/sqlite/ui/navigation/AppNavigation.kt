package com.example.sqlite.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sqlite.data.local.AppDatabase
import com.example.sqlite.data.repository.CartRepository
import com.example.sqlite.data.repository.ProductRepository
import com.example.sqlite.data.repository.UserRepository
import com.example.sqlite.ui.cart.CartScreen
import com.example.sqlite.ui.catalog.CatalogScreen
import com.example.sqlite.ui.login.LoginScreen
import com.example.sqlite.ui.register.RegisterScreen
import com.example.sqlite.viewmodel.*


@Composable
fun AppNavigation(
    database: AppDatabase
) {
    val navController = rememberNavController()
    var currentUserId by remember { mutableIntStateOf(0) }

    // Repositories
    val userRepository = remember { UserRepository(database.userDao()) }
    val productRepository = remember { ProductRepository(database.productDao()) }
    val cartRepository = remember { CartRepository(database.cartDao()) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(userRepository)
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { userId ->
                    currentUserId = userId
                    navController.navigate(Screen.Catalog.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Pantalla de Registro
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(userRepository)
            )

            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Catálogo
        composable(Screen.Catalog.route) {
            val catalogViewModel: CatalogViewModel = viewModel(
                factory = CatalogViewModelFactory(productRepository, cartRepository)
            )

            CatalogScreen(
                viewModel = catalogViewModel,
                userId = currentUserId,
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Pantalla de Carrito
        composable(Screen.Cart.route) {
            val cartViewModel: CartViewModel = viewModel(
                factory = CartViewModelFactory(cartRepository, currentUserId)
            )

            CartScreen(
                viewModel = cartViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

// Definición de rutas
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Catalog : Screen("catalog")
    object Cart : Screen("cart")
}