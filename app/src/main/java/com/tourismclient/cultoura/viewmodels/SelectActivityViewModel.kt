//package com.tourismclient.cultoura.viewmodels
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.tourismclient.cultoura.models.*
//import com.tourismclient.cultoura.utils.Result
//import com.tourismclient.cultoura.repositories.ItineraryRepository
//import kotlinx.coroutines.launch
//
///**
// * ViewModel for the DesignItineraryActivity
// */
//class SelectActivityViewModel(private val repository: ItineraryRepository = ItineraryRepository()) : ViewModel() {
//
//    private val _activitySections = MutableLiveData<Result<List<ActivitySection>>>()
//    val activitySections: LiveData<Result<List<ActivitySection>>> = _activitySections
//
//    private val _selectedActivities = MutableLiveData<MutableMap<Int, ActivityItem>>(mutableMapOf())
//    val selectedActivities: LiveData<MutableMap<Int, ActivityItem>> = _selectedActivities
//
//    private val _savedItinerary = MutableLiveData<Result<ItinerarySaveResponse>>()
//    val savedItinerary: LiveData<Result<ItinerarySaveResponse>> = _savedItinerary
//
//    private var planType: String? = null
//
//    /**
//     * Set the plan type to use for fetching activities
//     */
//    fun setPlanType(type: String) {
//        planType = type
//    }
//
//    /**
//     * Fetch all activity sections for the current plan type
//     */
//    fun fetchActivitySections() {
//        viewModelScope.launch {
//            _activitySections.value = Result.Loading
//
//            // Get all activities and group them by section
//            when (val result = repository.getActivities(planType)) {
//                is Result.Success -> {
//                    val activities = result.data
//                    val sections = groupActivitiesBySections(activities)
//                    _activitySections.value = Result.Success(sections)
//                }
//                is Result.Error -> {
//                    _activitySections.value = Result.Error(result.message)
//                }
//                else -> {
//                    // Do nothing for Loading
//                }
//            }
//        }
//    }
//
//    /**
//     * Select an activity for a section
//     */
//    fun selectActivity(sectionId: Int, activity: ActivityItem) {
//        val currentSelections = _selectedActivities.value ?: mutableMapOf()
//        currentSelections[sectionId] = activity
//        _selectedActivities.value = currentSelections
//    }
//
//    /**
//     * Check if all required sections have selected activities
//     */
//    fun isSelectionComplete(): Boolean {
//        val selections = _selectedActivities.value ?: return false
//        val sections = _activitySections.value?.getOrNull() ?: return false
//        return selections.size == sections.size
//    }
//
//    /**
//     * Create and save the final itinerary
//     */
//    fun saveItinerary(title: String, date: String) {
//        viewModelScope.launch {
//            _savedItinerary.value = Result.Loading
//
//            val selectedItems = _selectedActivities.value ?: return@launch
//            if (selectedItems.isEmpty()) {
//                _savedItinerary.value = Result.Error("No activities selected")
//                return@launch
//            }
//
//            // Create itinerary save request
//            val activityIds = selectedItems.values.map { it.id }
//            val saveRequest = ItinerarySaveRequest(
//                title = title,
//                planType = planType ?: "DEFAULT",
//                date = date,
//                activityIds = activityIds
//            )
//
//            // Save the itinerary
//            _savedItinerary.value = repository.saveItinerary(saveRequest)
//        }
//    }
//
//    /**
//     * Group activities by their sections
//     */
//    private fun groupActivitiesBySections(activities: List<ActivityItem>): List<ActivitySection> {
//        return activities.groupBy { it.sectionId }
//            .map { (sectionId, sectionActivities) ->
//                ActivitySection(
//                    id = sectionId,
//                    title = getSectionTitle(sectionId),
//                    activities = sectionActivities
//                )
//            }
//    }
//
//    /**
//     * Get title for a section based on ID
//     */
//    private fun getSectionTitle(sectionId: Int): String {
//        return when (sectionId) {
//            1 -> "Morning Activities"
//            2 -> "Lunch Options"
//            3 -> "Afternoon Activities"
//            4 -> "Evening Activities"
//            else -> "Activities"
//        }
//    }
//}