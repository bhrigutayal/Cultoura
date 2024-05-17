package com.example.tourismapp.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourismapp.Screens
import com.example.tourismapp.data.Result
import com.example.tourismapp.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun OnBoardPage( navController: NavController, homeViewModel: HomeViewModel){
    val result by homeViewModel.user.observeAsState()
    Box(modifier = Modifier
        .background(color = Color.Black)
        .height(200.dp)
        .width(200.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Text("Please Wait")
        }

    }

    LaunchedEffect(result) {
        if (result == null) {
            // Show loading screen
            delay(5000L)
            navController.navigate(Screens.SignUpScreen.route)

        } else {
            // Check if user was authenticated
            navController.navigate(Screens.HomePage.route)

        }
    }

}