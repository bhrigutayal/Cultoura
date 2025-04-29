//package com.tourismclient.cultoura.viewmodels
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.tourismclient.cultoura.models.PlanType
//import com.tourismclient.cultoura.repositories.ItineraryRepository
//import com.tourismclient.cultoura.utils.Result
//import kotlinx.coroutines.launch
//
///**
// * ViewModel for the CreatePlanActivity
// */
//class SelectPlanTypeViewModel(private val repository: ItineraryRepository = ItineraryRepository()) : ViewModel() {
//
//    private val _planTypes = MutableLiveData<Result<List<PlanType>>>()
//    val planTypes: LiveData<Result<List<PlanType>>> = _planTypes
//
//    private val _selectedPlanType = MutableLiveData<PlanType>()
//    val selectedPlanType: LiveData<PlanType> = _selectedPlanType
//
//    /**
//     * Fetch all available plan types from the API
//     */
//    fun fetchPlanTypes() {
//        viewModelScope.launch {
//            _planTypes.value = Result.Loading
//            _planTypes.value = repository.getPlanTypes()
//        }
//    }
//
//    /**
//     * Set the selected plan type
//     */
//    fun selectPlanType(planType: PlanType) {
//        _selectedPlanType.value = planType
//    }
//
//    /**
//     * Check if a plan type is selected
//     */
//    fun isPlanTypeSelected(): Boolean {
//        return _selectedPlanType.value != null
//    }
//}