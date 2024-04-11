package com.example.tourismapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tourismapp.Address
import com.example.tourismapp.R
import com.example.tourismapp.data.States
import com.example.tourismapp.viewmodel.HomeViewModel

@Composable
fun HomeView(paddingValues: PaddingValues, homeViewModel: HomeViewModel){
    var location by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues)
            .verticalScroll(rememberScrollState(), true),
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = location,
                singleLine = true,
                onValueChange = { location = it },
                placeholder =  { Text(text = "Location", color = Color.Black, fontSize = 1.sp) },
                modifier = Modifier.height(30.dp).width(300.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = colorResource(id = R.color.black),
                    // using predefined Color
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    // using our own colors in Res.Values.Color
                    focusedBorderColor = colorResource(id = R.color.black),
                    unfocusedBorderColor = colorResource(id = R.color.black),
                    focusedLabelColor = colorResource(id = R.color.black),
                    unfocusedLabelColor = colorResource(id = R.color.black),
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape= RoundedCornerShape(50)

            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        LazyRow( modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // This list has to be updated with real data
            val states = mutableListOf<States>()
            states.add(States("Maharashtra", Address.path))
            states.add(States("Gujarat", Address.path, Color.Red))
            states.add(States("Uttar Pradesh", Address.path, Color.Cyan))
            states.add(States("Haryana", Address.path, Color.Magenta))
            states.add(States("Delhi", Address.path, Color.Gray))
            states.add(States("Uttarakhand", Address.path, Color.Green))
            items(states){
                    state->
                StateRowItem(state = state, homeViewModel = homeViewModel)

            }
        }
        ListItems(heading = "Trips")
        ListItems(heading = "Foods")
        ListItems(heading = "Festivals")
    }
}