package com.tourismclient.cultoura.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tourismclient.cultoura.confidential.AuthTokenFetcher
import com.tourismclient.cultoura.databinding.ActivitySplashBinding
import com.tourismclient.cultoura.network.ApiUrl
import com.tourismclient.cultoura.utils.Constants
import com.tourismclient.cultoura.utils.SharedPreferences
import org.json.JSONObject
import java.util.Locale

class SplashActivity : BaseActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCityName()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCityName() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val cityName = addresses?.getOrNull(0)?.locality ?: "Unknown City"
                Log.e("CityName", "Current city: $cityName")
                Toast.makeText(this, "City: $cityName", Toast.LENGTH_LONG).show()
                SharedPreferences.setVariableInPreferences(Constants.CITY,cityName,this@SplashActivity)
            } ?: run {
                Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    private var binding : ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        AuthTokenFetcher().fetchAuthToken(this@SplashActivity)
        // This is used to align the xml view to this class
        setContentView(binding?.root)
        val city  = SharedPreferences.getVariablesInPreferences(Constants.CITY,this@SplashActivity)
        Log.e("CITY AT THE BEGGINING : ",city)
        if(city.isEmpty()){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface: Typeface =
            Typeface.createFromAsset(assets, "carbon bl.ttf")
        binding!!.tvAppName.typeface = typeface

        // Adding the handler to after the a task after some delay.
        Handler(Looper.getMainLooper()).postDelayed({

            // Here if the user is signed in once and not signed out again from the app. So next time while coming into the app
            // we will redirect him to MainScreen or else to the Intro Screen as it was before.

            // Get the current user id

            val currentUserID = SharedPreferences.getVariablesInPreferences(Constants.USER_TOKEN,this@SplashActivity)
            // Start the Intro Activity

            if (currentUserID.isNotEmpty()) {
                // Start the Main Activity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // Start the Intro Activity
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish() // Call this when your activity is done and should be closed.
        }, 2500) // Here we pass the delay time in milliSeconds after which the splash activity will disappear.
    }
}