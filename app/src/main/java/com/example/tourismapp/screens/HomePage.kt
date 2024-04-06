package com.example.tourismapp.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.tourismapp.R
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tourismapp.Address
import com.example.tourismapp.data.States

@Composable
fun HomePage(){
    BackHandler(enabled = true) {

    }
    var location by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = { TopAppBar()},
        bottomBar = {BottomAppBar()} ,
        scaffoldState = scaffoldState
    )
    {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = location,
                onValueChange = {location = it},
                label = { Text(text = "Location", color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = colorResource(id = R.color.black),
                    // using predefined Color
                    focusedTextColor = Color.Black,
                    unfocusedTextColor= Color.Black,
                    // using our own colors in Res.Values.Color
                    focusedBorderColor = colorResource(id = R.color.black),
                    unfocusedBorderColor = colorResource(id = R.color.black),
                    focusedLabelColor = colorResource(id = R.color.black),
                    unfocusedLabelColor = colorResource(id = R.color.black),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow( modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ) {
                val states = mutableListOf<States>()
                states.add(States("Maharashtra",Address.path))
                states.add(States("Gujarat",Address.path))
                items(states){
                    state->
                    StateRowItem(state = state,color= Color.Yellow)

                }
            }

        }

    }

    }

@Composable
fun StateRowItem(state:States, color: Color){
    Column(
        modifier= Modifier
            .size(150.dp)
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = rememberAsyncImagePainter(state.image) , contentDescription = null,modifier = Modifier
            .size(100.dp)
            .clip(
                CircleShape
            )
            .border(4.dp, color, CircleShape))
        Text(text = state.name, color = Color.Black)
    }
}