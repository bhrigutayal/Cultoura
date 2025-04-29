//package com.tourismclient.cultoura.viewmodels
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.tourismclient.cultoura.models.UserPreferences
//import com.tourismclient.cultoura.repositories.AuthRepository
//import com.tourismclient.cultoura.utils.Result
//import kotlinx.coroutines.launch
//
//class BudgetViewModel(private val authRepository: AuthRepository) : ViewModel() {
//
//    private val _preferences = MutableLiveData<UserPreferences>()
//    val preferences: LiveData<UserPreferences> = _preferences
//
//    private val _error = MutableLiveData<String>()
//    val error: LiveData<String> = _error
//
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> = _loading
//
//    fun getUserPreferences() {
//        viewModelScope.launch {
//            _loading.value = true
//            when (val result = authRepository.getUserPreferences()) {
//                is Result.Success -> {
//                    _preferences.value = result.data
//                }
//                is Result.Error -> {
//                    _error.value = result.message
//                }
//            }
//            _loading.value = false
//        }
//    }
//
//    fun updateBudget(budget: Double) {
//        viewModelScope.launch {
//            _loading.value = true
//
//            // Get current preferences first
//            val currentPrefs = _preferences.value ?: UserPreferences(budget = 0.0, travelDays = 0)
//
//            // Update only the budget
//            val updatedPrefs = currentPrefs.copy(budget = budget)
//
//            when (val result = authRepository.updateUserPreferences(updatedPrefs)) {
//                is Result.Success -> {
//                    _preferences.value = result.data
//                }
//                is Result.Error -> {
//                    _error.value = result.message
//                }
//            }
//            _loading.value = false
//        }
//    }
//}