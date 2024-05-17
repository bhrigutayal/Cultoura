package com.example.tourismapp.utils

import com.example.tourismapp.data.ItemsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder().baseUrl("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val DataService = retrofit.create(DatabaseApiService::class.java)

interface DatabaseApiService{
    @GET("Cultoura%20database.json?alt=media&token=7ee941a8-439a-44eb-b578-5e036f84beff")
    suspend fun getCategories() : ItemsResponse
}