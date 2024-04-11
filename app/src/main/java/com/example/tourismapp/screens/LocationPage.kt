package com.example.tourismapp.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LocationPage(paddingValues: PaddingValues, state: String?){
        if(state == null){
                // Discover Page
        }
        else{
                //Design According To State (Will use Switch case for selecting page of a state)
                Text("$state")
        }
        LazyColumn(
                modifier = Modifier.padding(paddingValues)
        ) {


        }

}