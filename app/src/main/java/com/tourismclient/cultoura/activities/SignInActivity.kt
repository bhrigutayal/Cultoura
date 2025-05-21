package com.tourismclient.cultoura.activities

import android.content.Intent
import  android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.android.volley.VolleyError
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.databinding.ActivitySignInBinding
import com.tourismclient.cultoura.models.User
import com.tourismclient.cultoura.network.ApiUrl
import com.tourismclient.cultoura.utils.Constants
import com.tourismclient.cultoura.utils.SharedPreferences
import com.tourismclient.cultoura.utils.VolleyRequest
import org.json.JSONObject

class SignInActivity : BaseActivity() {
    private var binding : ActivitySignInBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        // This is used to align the xml view to this class
        setContentView(binding?.root)

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


        setupActionBar()

        binding!!.btnSignIn.setOnClickListener {
            val email: String = binding!!.etEmail.text.toString().trim { it <= ' ' }
            val password: String = binding!!.etPassword.text.toString().trim { it <= ' ' }
            signInRegisteredUser(email,password)
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding!!.toolbarSignInActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        binding!!.toolbarSignInActivity.setNavigationOnClickListener { backPressedCallback.handleOnBackPressed() }
    }

    /**
     * A function for Sign-In using the registered user using the email and password.
     */
    fun signInRegisteredUser(email: String = "", password: String = "") {


        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            val volleyRequest = VolleyRequest()
            val jsonObject = JSONObject()
            jsonObject.put("username",email)
            jsonObject.put("password",password)
            volleyRequest.makePOSTRequest(
                ApiUrl.LOGIN,
                jsonObject,
                this@SignInActivity,
                false
            )
            volleyRequest.setVolleyRequestListener(object : VolleyRequest.VolleyRequestListener {
                override fun onDataLoaded(jsonObject: JSONObject) {
                   SharedPreferences.setVariableInPreferences(Constants.USER_TOKEN,jsonObject.getString("token"),this@SignInActivity)
                   signInSuccess(User(email, password))
                }

                override fun onError(error: VolleyError) {
                    showErrorSnackBar("Error Signing In")
                }
            })


        }
    }

    /**
     * A function to validate the entries of a user.
     */
    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }

    /**
     * A function to get the user details from the firestore database after authentication.
     */
    fun signInSuccess(user: User) {
        hideProgressDialog()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        this.finish()
    }
}


