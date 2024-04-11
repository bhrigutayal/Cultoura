package com.example.tourismapp.data

import androidx.compose.ui.graphics.Color
import com.example.tourismapp.Address

data class States(
    val name:String= "Delhi",
    val image: String = Address.path,
    val borderColor: Color = Color.Yellow
)

