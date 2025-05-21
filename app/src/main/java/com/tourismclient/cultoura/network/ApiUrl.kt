package com.tourismclient.cultoura.network

object ApiUrl {
    const val HOST = "https://8497-116-73-58-133.ngrok-free.app"
    const val SEND_NOTIFICATION = "https://fcm.googleapis.com/fcm/send"
    const val LOGIN = "${HOST}/api/auth/login"
    const val SIGNUP = "${HOST}/api/auth/register"
    const val GETACTIVITYOPTIONS = "${HOST}/api/itineraries/activity-choices"
}