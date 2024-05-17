package com.example.tourismapp.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tourismapp.MainActivity
import com.example.tourismapp.Screens
import com.example.tourismapp.data.ItenaryItem
import com.example.tourismapp.utils.LocationUtils
import com.example.tourismapp.viewmodel.HomeViewModel
import com.example.tourismapp.viewmodel.ItineraryViewModel
import com.example.tourismapp.viewmodel.LocationViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Itinerary(locationUtils: LocationUtils,
              viewModel:LocationViewModel,
              navController: NavController,
              paddingValues: PaddingValues,
              homeViewModel: HomeViewModel,
              itineraryViewModel: ItineraryViewModel
              ){
    val context = LocalContext.current
    var Startdate:LocalDate? by remember { mutableStateOf(null) }
    var Endate:LocalDate? by remember { mutableStateOf(null) }
    
    if(homeViewModel.showCalendar.value) {
     val state = rememberDateRangePickerState()
        DatePickerDialog(
            confirmButton = {
                Button(onClick = {
                   Startdate = state.selectedStartDateMillis?.let{
                       Instant.ofEpochMilli(it)
                       .atZone(ZoneId.systemDefault())
                       .toLocalDate()}
                    Endate = state.selectedEndDateMillis?.let{
                        Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()}
                    homeViewModel.showCalendar.value =false
                }) {
                    Text(text = "Select")
                }
            },
            content = @Composable { DateRangePicker(state = state)},
            onDismissRequest = {homeViewModel.showCalendar.value = false},
            dismissButton = { Button(onClick = { homeViewModel.showCalendar.value= false }) {
                Text(text = "Cancel")
            }}

        )
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions() ,
        onResult = { permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                // I HAVE ACCESS to location

                locationUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if(rationaleRequired){
                    Toast.makeText(context,
                        "Location Permission is required for this feature to work", Toast.LENGTH_LONG)
                        .show()
                }else{
                    Toast.makeText(context,
                        "Location Permission is required. Please enable it in the Android Settings",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    val location by viewModel.address.observeAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text=  location?.firstOrNull()?.formatted_address ?: "No Address")
            Button(onClick = {
                if (locationUtils.hasLocationPermission(context)) {
                    locationUtils.requestLocationUpdates(viewModel)
                    navController.navigate(Screens.LocationScreen.route) {
                        this.launchSingleTop
                    }
                } else {
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }) {
                Text(text = "Set A Location")
            }
        }
        Button(onClick = { homeViewModel.showCalendar.value = true}) {
            Text(text = "Set Dates")
        }
        val itemslist = itineraryViewModel.categoriesState.value.list
        LazyColumn {
            items(itemslist){
                item->
                ItineraryItem(item = item)
            }
        }


    }
    
}

@Composable
fun ItineraryItem(item: ItenaryItem){
Box(){
    Column {
        Image(painter = rememberAsyncImagePainter(model = item.image), contentDescription = null)
        Text(text = item.Price, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Normal, fontFamily = FontFamily.Monospace)
        Text(text = item.event, fontSize = 15.sp, fontWeight = FontWeight.Light, fontStyle = FontStyle.Normal, fontFamily = FontFamily.Monospace)
        Row(modifier = Modifier.background(color = Color.White)) {

            Text(text = item.NumberofPeople, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Normal, fontFamily = FontFamily.Monospace)
            Text(text = item.State, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Normal, fontFamily = FontFamily.Monospace)
            Text(text = item.FriendorFamily, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Normal, fontFamily = FontFamily.Monospace)
        }
    }
}
}