package com.tourismclient.cultoura.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.adapters.ActivitySelectionAdapter
import com.tourismclient.cultoura.databinding.ActivitySelectActivityBinding
import com.tourismclient.cultoura.models.ActivityItem
import com.tourismclient.cultoura.models.ActivitySection
import com.tourismclient.cultoura.models.Location
import com.tourismclient.cultoura.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectActivityActivity : AppCompatActivity() {

    private lateinit var activityAdapter: ActivitySelectionAdapter
    private lateinit var binding: ActivitySelectActivityBinding
    private val selectedActivities = mutableMapOf<Long, ActivityItem>()
    private val activitySections = mutableListOf<ActivitySection>()

    private val activityDetailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val sectionId = data?.getLongExtra("section_id", -1) ?: -1
            val activityId = data?.getLongExtra("activity_id", -1) ?: -1
            val activity = data?.getParcelableExtra<ActivityItem>("activity")

            if (sectionId != -1L && activityId != -1L && activity != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    onActivitySelected(sectionId, activity)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        CoroutineScope(Dispatchers.Main).launch {
            loadActivitySections()
            setupRecyclerView()
        }
        setupListeners()
    }

    private fun setupToolbar() {
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val menuButton = findViewById<ImageButton>(R.id.menuButton)

        backButton.setOnClickListener {
            onBackPressed()
        }

        menuButton.setOnClickListener {
            // Show menu options
            // This could be implemented as a popup menu
        }
    }

    private suspend fun loadActivitySections() {
        val planType = intent.getStringExtra(Constants.PLAN_TYPE) ?: ""
        val date = intent.getStringExtra(Constants.DATE) ?: ""
        val startTime = intent.getStringExtra(Constants.START_TIME) ?: ""
        val endTime = intent.getStringExtra(Constants.END_TIME) ?: ""
        val budget = intent.getStringExtra(Constants.BUDGET) ?: ""

        // TODO: Replace with actual API call
        activitySections.addAll(createDummyActivitySections())
    }

    private fun createDummyActivitySections(): List<ActivitySection> {
        // This is just for demonstration, replace with your actual data loading
        return listOf(
            ActivitySection(1L, "Morning Activities", listOf(
                ActivityItem(101L, "Morning Hike", "Enjoy a refreshing hike with beautiful views", R.drawable.ic_sample,1,4,400.0,4f,
                    Location(6.0, 5.6)
                ),ActivityItem(102L, "Morning Hike", "Enjoy a refreshing hike with beautiful views", R.drawable.ic_sample,1,4,400.0,4f,
                    Location(6.0, 5.6)
                ),ActivityItem(103L, "Morning Hike", "Enjoy a refreshing hike with beautiful views", R.drawable.ic_sample,1,4,400.0,4f,
                    Location(6.0, 5.6)
                )

            )),
            ActivitySection(2L, "Afternoon Activities", listOf(
                ActivityItem(201L, "Lunch Cruise", "Dine while enjoying waterfront views", R.drawable.ic_sample,2,4,400.0,4f,
                    Location(6.0, 5.6),

            ),ActivityItem(202L, "Morning Hike", "Enjoy a refreshing hike with beautiful views", R.drawable.ic_sample,2,4,400.0,4f,
                    Location(6.0, 5.6)
                ),ActivityItem(203L, "Morning Hike", "Enjoy a refreshing hike with beautiful views", R.drawable.ic_sample,2,4,400.0,4f,
                    Location(6.0, 5.6)
                )
            )),
            ActivitySection(3L, "Evening Activities", listOf(
                ActivityItem(301L, "Sunset View", "Watch the sunset from a scenic spot",  R.drawable.ic_sample,3,4,400.0,4f,
                    Location(6.0, 5.6)),ActivityItem(302L, "Morning Hike", "Enjoy a refreshing hike with beautiful views", R.drawable.ic_sample,3,4,400.0,4f,
                    Location(6.0, 5.6)
                ),ActivityItem(303L, "Morning Hike", "Enjoy a refreshing hike with beautiful views", R.drawable.ic_sample,3,4,400.0,4f,
                    Location(6.0, 5.6)
                )

            ))
                )
    }

    private fun setupRecyclerView() {
        activityAdapter = ActivitySelectionAdapter(
            activitySections,
            { sectionId, activityId, position ->
                // Open activity detail screen on click
                openActivityDetailScreen(sectionId, position)
            },
            { sectionId, activity ->
                // This will be called when an activity is selected from the detail screen
                CoroutineScope(Dispatchers.Main).launch {
                    onActivitySelected(sectionId, activity)
                }
            }
        )

        binding.sectionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SelectActivityActivity)
            adapter = activityAdapter
        }
    }

    private fun setupListeners() {
        binding.createItineraryButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (isSelectionComplete()) {
                    navigateToFinalItinerary()
                }
            }
        }
    }

    private fun openActivityDetailScreen(sectionId: Long, position: Int) {
        val section = activitySections.find { it.id == sectionId } ?: return
        val selectedId = selectedActivities[sectionId]?.id

        // Launch the detail activity with ViewPager
        val intent = ActivityDetailActivity.createIntent(
            this,
            sectionId,
            ArrayList(section.activities),
            selectedId,
            position  // This position comes from the adapter and represents the clicked item
        )
        activityDetailLauncher.launch(intent)
    }

    private suspend fun onActivitySelected(sectionId: Long, activity: ActivityItem) {
        // Store the selected activity for this section
        if (selectedActivities.containsKey(sectionId) && selectedActivities[sectionId]?.id == activity.id) {
            selectedActivities.remove(sectionId)

            // Update the adapter to reflect deselection
            activityAdapter.updateSelection(sectionId, activity.id, true)
        } else {
            selectedActivities[sectionId] = activity

            // Update the adapter to reflect selection
            activityAdapter.updateSelection(sectionId, activity.id, false)
        }

        // Update create button state
        updateCreateButtonState()
    }

    private suspend fun updateCreateButtonState() {
        binding.createItineraryButton.isEnabled = isSelectionComplete()

        // Update visual state
        if (isSelectionComplete()) {
            binding.createItineraryButton.setBackgroundColor(resources.getColor(R.color.black, null))
        } else {
            binding.createItineraryButton.setBackgroundColor(resources.getColor(R.color.grey, null))
        }
    }

    private suspend fun isSelectionComplete(): Boolean {
        // Check if user has selected at least one activity
        return selectedActivities.isNotEmpty()

        // Optional: Check if user has selected an activity from each section
        // return activitySections.size == selectedActivities.size
    }

    private fun navigateToFinalItinerary() {
        val intent = Intent(this, FinalItineraryActivity::class.java).apply {
            putExtra("SELECTED_ACTIVITIES", ArrayList(selectedActivities.values.map { it.id }))
            putExtra(Constants.PLAN_TYPE, intent.getStringExtra(Constants.PLAN_TYPE) ?: "")
            putExtra(Constants.DATE, intent.getStringExtra(Constants.DATE) ?: "")
            putExtra(Constants.START_TIME, intent.getStringExtra(Constants.START_TIME) ?: "")
            putExtra(Constants.END_TIME, intent.getStringExtra(Constants.END_TIME) ?: "")
            putExtra(Constants.BUDGET, intent.getIntExtra(Constants.BUDGET,0))
        }
        startActivity(intent)
    }
}