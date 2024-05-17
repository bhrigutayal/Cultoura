package com.example.tourismapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourismapp.Screens
import com.example.tourismapp.data.User
import com.example.tourismapp.viewmodel.HomeViewModel


@Composable
fun ProfilePage(
        paddingValues: PaddingValues,
                homeViewModel: HomeViewModel,
        navController: NavController
){
        val user = homeViewModel.user.observeAsState()

        Column(modifier= Modifier
                .fillMaxSize()
                .padding(paddingValues)
                ,horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(45.dp))
                Text(text = "${user.value?.email}", color = Color.Black)
                Text(text = "${user.value?.firstName}", color = Color.Black)
                Text(text = "${user.value?.lastName}", color = Color.Black)
                Button(onClick = { homeViewModel.signOut()
                        navController.navigate(Screens.LoginScreen.route)


                }) {
                        Text(text = "SignOut")
                }
        }


}
