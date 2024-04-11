package com.example.tourismapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tourismapp.data.Result
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.tourismapp.Injection
import com.example.tourismapp.data.User
import com.example.tourismapp.data.Result.*
import com.example.tourismapp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {
    private val userRepository: UserRepository
    private val _user = MutableLiveData<User>()
    val user : LiveData<User> get() = _user
    val selected = MutableLiveData<String>()
    val state = MutableLiveData<String>()
init{
    userRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )
    loadCurrentUser()
    selected.value = "Home"
}

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Success -> _user.value = result.data
                is Error -> {

                }

            }
        }
    }
    fun Navigate(value:String){
        selected.value = value
    }
    fun setState(value:String){
        state.value = value
    }
}