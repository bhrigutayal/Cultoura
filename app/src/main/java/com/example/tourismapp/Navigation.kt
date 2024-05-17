package com.example.tourismapp

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.tourismapp.screens.HomePage
import com.example.tourismapp.screens.LoadingScreen
import com.example.tourismapp.screens.LocationSelectionScreen
import com.example.tourismapp.screens.LoginScreen
import com.example.tourismapp.screens.OnBoardPage
import com.example.tourismapp.screens.SignUpScreen
import com.example.tourismapp.utils.LocationUtils
import com.example.tourismapp.viewmodel.AuthViewModel
import com.example.tourismapp.viewmodel.HomeViewModel
import com.example.tourismapp.viewmodel.ItineraryViewModel
import com.example.tourismapp.viewmodel.LocationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel= viewModel(),
    locationViewModel: LocationViewModel= viewModel(),
    context: Context = LocalContext.current,
    itineraryViewModel: ItineraryViewModel = viewModel()



) {

    val locationUtils = LocationUtils(context)

    NavHost(
        navController = navController,
        startDestination = Screens.onBoarding.route
    ) {
        composable(Screens.SignUpScreen.route) {
            SignUpScreen(
                onNavigateToLogin = { navController.navigate(Screens.LoginScreen.route) }
            )
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(
                authViewModel,
                onNavigateToSignUp = {navController.navigate(Screens.SignUpScreen.route) }
            ){
                navController.navigate(Screens.Loading.route){
                    this.launchSingleTop
                }
            }
        }
        composable(Screens.HomePage.route){
                  HomePage(homeViewModel,locationUtils,locationViewModel,navController,itineraryViewModel)
        }
        composable(Screens.onBoarding.route){
            OnBoardPage(navController = navController, homeViewModel = homeViewModel)
        }

        dialog(Screens.LocationScreen.route){
            locationViewModel.location.value?.let{it1 ->

                LocationSelectionScreen(location = it1, onLocationSelected = {locationdata->
                    locationViewModel.fetchAddress("${locationdata.latitude},${locationdata.longitude}")
                    navController.popBackStack()
                }, goBack = {navController.popBackStack()},locationUtils)
            }
        }
        dialog(Screens.Loading.route){
            LoadingScreen(authViewModel = authViewModel, navController = navController)
        }

    }
}
