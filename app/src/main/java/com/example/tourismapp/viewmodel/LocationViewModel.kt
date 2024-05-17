package com.example.tourismapp.viewmodel


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismapp.RetrofitClient
import kotlinx.coroutines.launch
import com.example.tourismapp.data.*
class LocationViewModel: ViewModel() {
    private  val _location= mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    private val _address = MutableLiveData<List<GeocodingResult>>()
    val address: LiveData<List<GeocodingResult>> = _address


    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latlng: String){
        try{
            viewModelScope.launch {
                val result = RetrofitClient.create().getAddressFromCoordinates(
                    latlng,
                    "AIzaSyCfX_kZTPSauurgzcx4Mg3nHdmIo3bpBWs"
                )
                _address.value = result.results
            }
        }catch(e:Exception) {
            Log.d("res1", "${e.cause} ${e.message}")
        }
    }

}