package com.example.tourismapp.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.example.tourismapp.Address
import com.example.tourismapp.R
import com.example.tourismapp.data.Result
import com.example.tourismapp.viewmodel.AuthViewModel



@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess:()->Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember {
        mutableStateOf("")
    }

    val result by authViewModel.authResult.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = stringResource(R.string.app_name), fontWeight = FontWeight.Bold, color= Color.Black, fontSize = 45.sp)
       Image(painter = rememberAsyncImagePainter(Address.path) , contentDescription = null,modifier = Modifier.size(200.dp).clip(
           CircleShape).border(4.dp,Color.Yellow,CircleShape))
        Text(text = "Festival of India", fontWeight = FontWeight.Light, color= Color.Gray)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.black),
                // using predefined Color
                focusedTextColor = Color.Black,
                unfocusedTextColor= Color.Black,
                // using our own colors in Res.Values.Color
                unfocusedContainerColor = colorResource(id = R.color.text_field),
                focusedContainerColor = colorResource(id = R.color.text_field),
                focusedBorderColor = colorResource(id = R.color.black),
                unfocusedBorderColor = colorResource(id = R.color.black),
                focusedLabelColor = colorResource(id = R.color.black),
                unfocusedLabelColor = colorResource(id = R.color.black),
            )

        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.black),
                // using predefined Color
                focusedTextColor = Color.Black,
                unfocusedTextColor= Color.Black,
                // using our own colors in Res.Values.Color
                unfocusedContainerColor = colorResource(id = R.color.text_field),
                focusedContainerColor = colorResource(id = R.color.text_field),
                focusedBorderColor = colorResource(id = R.color.black),
                unfocusedBorderColor = colorResource(id = R.color.black),
                focusedLabelColor = colorResource(id = R.color.black),
                unfocusedLabelColor = colorResource(id = R.color.black),
            )

        )
        Button(
            onClick = {

              authViewModel.login(email, password)
                when (result) {
                    is Result.Success->{
                        onSignInSuccess()
                    }
                    is Result.Error ->{

                    }

                    else -> {

                    }
                }


            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(Color.Green)
        ) {
            Text("Login")
        }
        Text("Forgot Password?", modifier = Modifier.clickable {  }, textDecoration = TextDecoration.Underline)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account? Sign up.",
           modifier = Modifier.clickable { onNavigateToSignUp() }
        )
    }
}




