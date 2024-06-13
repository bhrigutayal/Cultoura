package com.example.tourismapp.viewmodel


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismapp.data.ItenaryItem
import com.example.tourismapp.utils.DataService
import kotlinx.coroutines.launch
import java.lang.Exception

class ItineraryViewModel : ViewModel (){

    private val _categoriesState = mutableStateOf(DataState())
    val categoriesState : State<DataState> = _categoriesState
    init {
        fetchCategories()
    }

    private fun fetchCategories(){
        viewModelScope.launch {
            try{
                val response = DataService.getCategories()
                _categoriesState.value = _categoriesState.value.copy(
                    list= response.categories,
                    loading = false,
                    error = null
                )

            }catch (e: Exception){
                _categoriesState.value= _categoriesState.value.copy(
                    loading = false,
                    error= "Error fetching Categories ${e.message}"
                )
            }
        }
    }

    data class DataState(
        val loading: Boolean = true,
        val list: List<ItenaryItem> = emptyList(),
        val error: String? = null
    )
}