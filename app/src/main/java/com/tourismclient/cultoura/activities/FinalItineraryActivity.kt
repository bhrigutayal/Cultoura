package com.tourismclient.cultoura.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.adapters.ItineraryAdapter
import com.tourismclient.cultoura.models.ActivityItem
import com.tourismclient.cultoura.models.ActivityType
import com.tourismclient.cultoura.models.DayPlan
import com.tourismclient.cultoura.models.Itinerary
import com.tourismclient.cultoura.models.ItineraryItem
import com.tourismclient.cultoura.models.Location
import com.tourismclient.cultoura.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FinalItineraryActivity : AppCompatActivity() {

    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var costTextView: TextView
    private lateinit var itineraryRecyclerView: RecyclerView
    private lateinit var viewCustomItineraryButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var downloadButton: ImageButton
    private lateinit var editButton: ImageButton

    private lateinit var itineraryAdapter: ItineraryAdapter
    private var itineraryItems = mutableListOf<ActivityItem>()
    private var currentItinerary: Itinerary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_itinerary)

        initViews()
        setupData()
        setupAdapter()
        setupListeners()
    }

    private fun initViews() {
        dateTextView = findViewById(R.id.dateTextView)
        timeTextView = findViewById(R.id.timeTextView)
        costTextView = findViewById(R.id.costTextView)
        itineraryRecyclerView = findViewById(R.id.itineraryRecyclerView)
        viewCustomItineraryButton = findViewById(R.id.viewCustomItineraryButton)
        backButton = findViewById(R.id.backButton)
        downloadButton = findViewById(R.id.downloadButton)
        editButton = findViewById(R.id.editButton)

        // Setup RecyclerView
        itineraryRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupData() {
        // Get data from intent
        val displayDate = intent.getStringExtra(Constants.DATE) ?: "Not specified"
        val displayStartTime = intent.getStringExtra(Constants.START_TIME) ?: "9:00 AM"
        val displayEndTime = intent.getStringExtra(Constants.END_TIME) ?: "6:00 PM"
        val budget = intent.getIntExtra(Constants.BUDGET,150)

        // Set data to views
        dateTextView.text = displayDate
        timeTextView.text = "$displayStartTime - $displayEndTime"
        costTextView.text = "$${budget}"

        // Generate itinerary items based on the inputs
        generateItineraryItems(displayStartTime, displayEndTime, budget)

        // Convert to Itinerary object
        convertToItineraryObject(displayDate, displayStartTime, displayEndTime, budget)
    }

    private fun setupAdapter() {
        // Define the three callbacks needed for the adapter

        // 1. When an itinerary item is clicked
        val onItemClick: (Itinerary) -> Unit = { itinerary ->
            // Handle item click
            Toast.makeText(this, "Viewing details for: ${itinerary.title}", Toast.LENGTH_SHORT).show()

            // You might want to navigate to a detail activity
            // val intent = Intent(this, ItineraryDetailActivity::class.java)
            // intent.putExtra("itinerary_id", itinerary.id)
            // startActivity(intent)
        }

        // 2. When the share button is clicked
        val onShareClick: (Itinerary, View) -> Unit = { itinerary, view ->
            // Create a share intent
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,
                    "Check out my itinerary: ${itinerary.title} in ${itinerary.destination}\n" +
                            "From ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(itinerary.startDate)} " +
                            "to ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(itinerary.endDate)}")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share Itinerary"))
        }

        // 3. When the delete button is clicked
        val onDeleteClick: (Itinerary) -> Unit = { itinerary ->
            // Show confirmation dialog before deleting
            AlertDialog.Builder(this)
                .setTitle("Delete Itinerary")
                .setMessage("Are you sure you want to delete ${itinerary.title}?")
                .setPositiveButton("Delete") { dialog, _ ->
                    // Delete the itinerary
                    // In a real app, you would delete from database
                    // For now, just show a toast
                    Toast.makeText(this, "Itinerary deleted", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                    // Return to previous screen
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // Initialize the adapter with the callbacks
        itineraryAdapter = ItineraryAdapter(onItemClick, onShareClick, onDeleteClick)

        // Set the adapter to the RecyclerView
        itineraryRecyclerView.adapter = itineraryAdapter

        // Submit the current itinerary
        currentItinerary?.let {
            itineraryAdapter.submitList(listOf(it))
        }
    }

    private fun generateItineraryItems(startTime: String, endTime: String, budget: Int) {
        // This is your existing method to generate ItineraryItem objects
        // (Keep the existing implementation for backward compatibility)
        itineraryItems.clear()

        // Parse times to calculate duration
        val startTimeMillis = parseTimeToMillis(startTime)
        val endTimeMillis = parseTimeToMillis(endTime)

        // Calculate total hours available
        val totalDurationMillis = endTimeMillis - startTimeMillis
        val totalHours = totalDurationMillis / (60 * 60 * 1000)

        // Create activities based on budget and duration
        var currentTimeMillis = startTimeMillis
        var remainingBudget = budget

        val activities = generateActivitiesByBudget(budget, totalHours.toInt())

        for (activity in activities) {
            val activityDurationMillis = activity.duration * 60 * 60 * 1000
            val endActivityTimeMillis = currentTimeMillis + activityDurationMillis

            itineraryItems.add(
                ActivityItem(
                    id = activity.id,
                    title = activity.title,
                    description = activity.description,
                    imageUrl = activity.imageUrl,
                    sectionId = 2,
                    duration = activity.duration,
                    cost = activity.cost,
                    rating = 5f,
                    location = Location(
                        latitude = 0.0, longitude = 0.3),
                )
            )

            currentTimeMillis = endActivityTimeMillis
            remainingBudget -= activity.cost.toInt()

            // Add travel time between activities
            if (activity != activities.last()) {
                currentTimeMillis += 30 * 60 * 1000 // 30 minutes travel time
            }

            // Stop if we exceed the end time
            if (currentTimeMillis > endTimeMillis) break
        }
    }

    private fun convertToItineraryObject(displayDate: String, startTime: String, endTime: String, budget: Int) {
        // Parse the date
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val startDate = try {
            dateFormat.parse(displayDate) ?: Date()
        } catch (e: Exception) {
            Date() // Fallback to current date if parsing fails
        }

        // Create end date (same as start date for a day itinerary)
        val endDate = startDate

        // Convert itineraryItems to Activity objects

        // Create a day plan
        val dayPlan = DayPlan(
            dayNumber = 1,
            date = startDate,
            activities = itineraryItems,
            title = "Day 1 in ${determineDestination()}"
        )

        // Create the itinerary
        currentItinerary = Itinerary(
            title = "Custom Itinerary for $displayDate",
            destination = determineDestination(),
            startDate = startDate,
            endDate = endDate,
            description = "Generated itinerary for your visit from $startTime to $endTime with a budget of $$budget",
            dayPlans = listOf(dayPlan),
            budget = budget.toDouble(),
            coverImagePath = R.drawable.ic_sample.toString()
        )
    }

    // Helper method to determine activity type based on title (simplified)
    private fun determineActivityType(title: String): ActivityType {
        return when {
            title.contains("Coffee") || title.contains("Lunch") ||
                    title.contains("Dinner") || title.contains("Food") -> ActivityType.RESTAURANT
            title.contains("Museum") || title.contains("Historical") ||
                    title.contains("Tour") || title.contains("Boat") -> ActivityType.ATTRACTION
            title.contains("Shopping") || title.contains("Market") -> ActivityType.SHOPPING
            else -> ActivityType.OTHER
        }
    }

    // Helper method to determine destination (could be improved with real data)
    private fun determineDestination(): String {
        return "Local City" // Replace with actual destination if available
    }

    // Existing helper methods
    private fun parseTimeToMillis(timeString: String): Long {
        try {
            val format = SimpleDateFormat("h:mm a", Locale.getDefault())
            val date = format.parse(timeString)
            val calendar = Calendar.getInstance()

            date?.let {
                calendar.time = it
                // Set to today's date
                val today = Calendar.getInstance()
                calendar.set(Calendar.YEAR, today.get(Calendar.YEAR))
                calendar.set(Calendar.MONTH, today.get(Calendar.MONTH))
                calendar.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))
                return calendar.timeInMillis
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private fun formatMillisToTime(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val format = SimpleDateFormat("h:mm a", Locale.getDefault())
        return format.format(calendar.time)
    }

    private fun generateActivitiesByBudget(budget: Int, hours: Int): List<ActivityItem> {
        // Keep your existing implementation
        val allActivities = listOf(
            ActivityItem(
                1,
                "Coffee at Local Cafe",
                "Start your day with a freshly brewed coffee and pastry",
                15,
                1,
                R.drawable.ic_sample,
                cost = 56.0,
                rating = 4f,
                location = Location(4.0,3.6)
            ),
            ActivityItem(
                2,
                "Coffee at Local Cafe",
                "Start your day with a freshly brewed coffee and pastry",
                15,
                1,
                R.drawable.ic_sample,
                cost = 56.0,
                rating = 4f,
                location = Location(4.0,3.6)
            ),
            ActivityItem(
                1,
                "Coffee at Local Cafe",
                "Start your day with a freshly brewed coffee and pastry",
                15,
                1,
                R.drawable.ic_sample,
                cost = 56.0,
                rating = 4f,
                location = Location(4.0,3.6)
            )

        )

        // Algorithm to select activities based on budget and time
        val selectedActivities = mutableListOf<ActivityItem>()
        var remainingBudget = budget
        var remainingHours = hours.toDouble()

        // Add mandatory activities first (like meals)
        val breakfast = allActivities.find { it.id == 1L }
        breakfast?.let {
            selectedActivities.add(it)
            remainingBudget -= it.cost.toInt()
            remainingHours -= it.duration
        }

        val lunch = allActivities.find { it.id == 3L }
        lunch?.let {
            selectedActivities.add(it)
            remainingBudget -= it.cost.toInt()
            remainingHours -= it.duration
        }

        // If budget and time allow, add dinner
        if (remainingBudget >= 45 && remainingHours >= 2) {
            val dinner = allActivities.find { it.id == 8L }
            dinner?.let {
                selectedActivities.add(it)
                remainingBudget -= it.cost.toInt()
                remainingHours -= it.duration
            }
        }

        // Add other activities based on remaining budget and time
        val otherActivities = allActivities.filter { it.id !in listOf(1L, 3L, 8L) }
            .sortedBy { it.cost }

        for (activity in otherActivities) {
            if (remainingBudget >= activity.cost && remainingHours >= activity.duration) {
                selectedActivities.add(activity)
                remainingBudget -= activity.cost.toInt()
                remainingHours -= activity.duration
            }

            // Stop if we've filled the day
            if (remainingHours < 1) break
        }

        // Sort activities by a logical order for the day
        return selectedActivities.sortedBy {
            when (it.id) {
                1L -> 0 // Breakfast first
                3L -> 3 // Lunch in the middle
                8L -> 6 // Dinner at the end
                else -> it.id
            }
        }
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            // Go back to start
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        downloadButton.setOnClickListener {
            // Mock download functionality
            Toast.makeText(this, "Itinerary downloaded to your device", Toast.LENGTH_SHORT).show()
        }

        editButton.setOnClickListener {
            // Go back to first step to edit
            finish()
        }

        viewCustomItineraryButton.setOnClickListener {
            // In a real app, this would navigate to a more detailed view
            Toast.makeText(this, "Viewing detailed itinerary", Toast.LENGTH_SHORT).show()
        }
    }
}