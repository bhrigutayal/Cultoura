package com.example.tourismapp.screens

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.tourismapp.R


@Composable
fun TopAppBar(){
    val navigationIcon : (@Composable () -> Unit)? =
            {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }



    TopAppBar(
        title = {
            Text(text = "Welcome",
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp))
        },
        elevation = 3.dp,
        backgroundColor = colorResource(id = R.color.text_field),
        navigationIcon = navigationIcon
    )
}