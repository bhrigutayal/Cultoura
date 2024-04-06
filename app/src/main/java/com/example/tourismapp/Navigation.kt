package com.example.tourismapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tourismapp.screens.HomePage
import com.example.tourismapp.screens.LoginScreen
import com.example.tourismapp.screens.SignUpScreen
import com.example.tourismapp.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()

) {
    NavHost(
        navController = navController,
        startDestination = Screens.SignUpScreen.route
    ) {
        composable(Screens.SignUpScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screens.LoginScreen.route) }
            )
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screens.SignUpScreen.route) }
            ) {
                   navController.navigate(Screens.HomePage.route)
            }
        }
        composable(Screens.HomePage.route){
            HomePage()
        }

    }
}
