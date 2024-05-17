package com.example.tourismapp

sealed class Screens(val route:String){
    data object  LoginScreen : Screens("login-screen")
    data object  SignUpScreen : Screens("signup-screen")
    data object HomePage:Screens("homepage")
    data object LocationScreen: Screens("location screen")
    data object Loading: Screens("Loading")

    data object onBoarding:Screens("OnBoarding")
}