package com.example.tourismapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.tourismapp.viewmodel.AuthViewModel
import com.example.tourismapp.data.Result.*

@Composable
fun LoadingScreen(authViewModel: AuthViewModel,navController: NavController) {
    val context = LocalContext.current
    val result by authViewModel.authResult.observeAsState()
    Box(modifier = Modifier.background(color = Color.Black).height(200.dp).width(200.dp), contentAlignment = Alignment.Center) {
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
        } else {
            // Proceed with your logic
            when(result){
                is Success -> {
                    navController.navigate(Screens.HomePage.route)
                }
                is Error -> {
                    navController.navigateUp()
                    authViewModel.reset()
                    Toast.makeText(context,"Incorrect Id or Password",Toast.LENGTH_LONG).show()
                }

                null -> TODO()
            }
        }
    }
}