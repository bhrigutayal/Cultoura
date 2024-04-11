package com.example.tourismapp.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.tourismapp.data.User

@Composable
fun ProfilePage(paddingValues: PaddingValues, user: User?){

        LazyColumn(
                modifier = Modifier.padding(paddingValues)
        ) {

        }

}