package com.example.tourismapp.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
fun Itinerary(
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel,
    navController: NavController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    itineraryViewModel: ItineraryViewModel
) {
    val context = LocalContext.current
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val state = rememberDateRangePickerState()
    // Display date picker dialog if needed
    if (homeViewModel.showCalendar.value) {
        DatePickerDialog(
            confirmButton = {
                Button(onClick = {

                    startDate = state.selectedStartDateMillis?.toLocalDate()
                    endDate = state.selectedEndDateMillis?.toLocalDate()
                    homeViewModel.showCalendar.value = false
                }) {
                    Text(text = "Select")
                }
            },
            content = { DateRangePicker(state = state) },
            onDismissRequest = { homeViewModel.showCalendar.value = false },
            dismissButton = {
                Button(onClick = { homeViewModel.showCalendar.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    // Request location permissions and handle the result
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            handlePermissionResult(permissions, context, locationUtils, locationViewModel)
        }
    )

    val location by locationViewModel.address.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = location?.firstOrNull()?.formatted_address ?: "No Address",
                color = Color.Black
            )
            Button(onClick = {
                if (locationUtils.hasLocationPermission(context)) {
                    locationUtils.requestLocationUpdates(locationViewModel)
                    navController.navigate(Screens.LocationScreen.route) {
                        launchSingleTop = true
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
        Button(onClick = { homeViewModel.showCalendar.value = true }) {
            Text(text = "Set Dates")
        }

        var loadItinerary by remember { mutableStateOf(false) }
        val viewState by itineraryViewModel.categoriesState

        if (loadItinerary) {
            StateLoader(viewState)
        }

        Button(onClick = { loadItinerary = true }) {
            Text(text = "Search Events / Trips")
        }
    }
}

@Composable
fun StateLoader(viewState: ItineraryViewModel.DataState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            viewState.error != null -> {
                Text(
                    text = "ERROR OCCURRED",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                ListOfItinerary(viewState.list)
            }
        }
    }
}

@Composable
fun ListOfItinerary(categories: List<ItenaryItem>) {
    LazyColumn {
        items(categories) { category ->
            ItineraryItem(item = category)
        }
    }
}

@Composable
fun ItineraryItem(item: ItenaryItem) {
    Box {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = item.image),
                contentDescription = null
            )
            Text(
                text = item.Price,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = item.event,
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Monospace
            )
            Row(modifier = Modifier.background(color = Color.White)) {
                Text(
                    text = item.NumberofPeople,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = item.State,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = item.FriendorFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

// Helper function to convert milliseconds to LocalDate
@RequiresApi(Build.VERSION_CODES.O)
private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

// Helper function to handle location permission results
private fun handlePermissionResult(
    permissions: Map<String, Boolean>,
    context: Context,
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel
) {
    if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
        permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    ) {
        locationUtils.requestLocationUpdates(locationViewModel)
    } else {
        val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
            context as Activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) || ActivityCompat.shouldShowRequestPermissionRationale(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val message = if (rationaleRequired) {
            "Location Permission is required for this feature to work"
        } else {
            "Location Permission is required. Please enable it in the Android Settings"
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
