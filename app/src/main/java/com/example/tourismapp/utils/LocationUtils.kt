package com.example.tourismapp.utils


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.tourismapp.data.LocationData
import com.example.tourismapp.viewmodel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient
            = LocationServices.getFusedLocationProviderClient(context)


    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationData(latitude = it.latitude, longitude =  it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }


    fun hasLocationPermission(context: Context):Boolean{

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }


    fun reverseGeocodeLocation(location: LocationData) : String{
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(location.latitude, location.longitude)
        val addresses:MutableList<Address>? =
            geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)

        return if(addresses?.isNotEmpty() == true){
            addresses[0].getAddressLine(0)
        }else{
            "Address not found"
        }
    }
    fun GeocodeLocation(location: String, max:Int) : MutableList<String>{
        val geocoder = Geocoder(context, Locale.getDefault())
       // val coordinate = LatLng(location.latitude, location.longitude)
        val list = mutableListOf<String>()
        val addresses:MutableList<Address>? =
            geocoder.getFromLocationName(location,max)


        return if(addresses?.isNotEmpty() == true){
           for( address  in addresses){
               list.add(address.getAddressLine(0))
           }
            list
        }else{
            list
        }

    }
    fun GetAddress(location: String) : LatLng{
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: MutableList<Address>? = geocoder.getFromLocationName(location, 1)
        return if(addresses?.isNotEmpty() == true){
            val address: Address = addresses[0]
            val lat: Double = address.latitude
            val lng: Double = address.longitude
            LatLng(lat,lng)
        }else{
            LatLng(0.0,0.0)
        }

    }

}