package com.example.tourismapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tourismapp.data.Images

@Composable
fun DiscoverPage(state:String = "", paddingValues: PaddingValues){
    Column(modifier = Modifier.verticalScroll(rememberScrollState(),true).padding(paddingValues)) {
        Text(text = if(state=="")"Discover" else state, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Normal, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Start, color = Color.Black)
        Spacer(modifier = Modifier.height(40.dp))
        val list = mutableListOf<Images>()
        list.add(Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2FUntitled%20Design.png?alt=media&token=14e818d4-71c3-4622-8ca6-256e9b524dda"))
        list.add(Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2FUntitled%20Design.jpg?alt=media&token=55386474-338f-490b-8a52-e5e24846d946"))
        list.add(Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2FUntitled%20Design%20(1).jpg?alt=media&token=597b7cec-ace5-4dcf-b487-3d4fd4f52f89"))
        ListItems(heading = if(state=="")"Plan a Hangout" else "Current Events")
        ListItems(heading = if(state=="")"Long Weekend Trip" else "Upcoming Events")
        ListItems(heading = if(state=="")"Vacation Ideas" else "Underrated Locations" )
    }
}