package com.example.tourismapp.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tourismapp.data.LocationData
import com.example.tourismapp.utils.LocationUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionScreen(
    location: LocationData,
    onLocationSelected: (LocationData) -> Unit,
    goBack: ()->Unit,
    locationUtils: LocationUtils
    ){
    val userLocation = remember{
        mutableStateOf(LatLng(location.latitude, location.longitude))
    }
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }
    var expanded by remember { mutableStateOf(false) }
    var selectedlocation by remember { mutableStateOf("") }
    val options = mutableListOf<String>()

    Column(modifier= Modifier.fillMaxSize()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedlocation,
                onValueChange = {
                    selectedlocation = it
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { IconButton(onClick = {
                    val addressnames = locationUtils.GeocodeLocation(selectedlocation,5)
                    for(i in 0..4){
                        options.add(addressnames[i])
                    }

                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }},
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        userLocation.value = locationUtils.GetAddress(option)
                        expanded = false
                    }) {
                        Text(option)
                    }
                }
            }
        }

        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                userLocation.value = it
            }
        ){
            Marker(state = MarkerState(position = userLocation.value))
        }

        var newLocation: LocationData

        Row(modifier= Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                newLocation =
                    LocationData(userLocation.value.latitude, userLocation.value.longitude)
                onLocationSelected(newLocation)
            }) {
                Text("Set Location")
            }
            Button(onClick = {
                goBack()
            }) {
                Text("Skip")
            }
        }
    }
}
