package com.example.sqlite.ui.navigation

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sqlite.data.local.AppDatabase
import com.example.sqlite.data.repository.CartRepository
import com.example.sqlite.data.repository.ProductRepository
import com.example.sqlite.data.repository.UserRepository
import com.example.sqlite.ui.camera.CameraScreen
import com.example.sqlite.ui.cart.CartScreen
import com.example.sqlite.ui.catalog.CatalogScreen
import com.example.sqlite.ui.login.LoginScreen
import com.example.sqlite.ui.register.RegisterScreen
import com.example.sqlite.viewmodel.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    database: AppDatabase
) {
    val navController = rememberNavController()
    var currentUserId by remember { mutableIntStateOf(0) }
    var capturedPhoto by remember { mutableStateOf<Bitmap?>(null) }

    val userRepository = remember { UserRepository(database.userDao()) }
    val productRepository = remember { ProductRepository(database.productDao()) }
    val cartRepository = remember { CartRepository(database.cartDao()) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -300 },
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        }
    ) {
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

        composable(Screen.Catalog.route) {
            val catalogViewModel: CatalogViewModel = viewModel(
                factory = CatalogViewModelFactory(productRepository, cartRepository)
            )

            CatalogScreen(
                viewModel = catalogViewModel,
                userId = currentUserId,
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route)
                },
                capturedPhoto = capturedPhoto
            )
        }

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

        composable(Screen.Camera.route) {
            CameraScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPhotoTaken = { bitmap ->
                    capturedPhoto = bitmap
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Catalog : Screen("catalog")
    object Cart : Screen("cart")
    object Camera : Screen("camera")
}
