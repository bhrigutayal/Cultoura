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
import com.tourismclient.cultoura.models.*
import com.tourismclient.cultoura.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

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

        itineraryRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupData() {
        val displayDate = intent.getStringExtra(Constants.DATE) ?: "Not specified"
        val displayStartTime = intent.getStringExtra(Constants.START_TIME) ?: "9:00 AM"
        val displayEndTime = intent.getStringExtra(Constants.END_TIME) ?: "6:00 PM"
        val budget = intent.getIntExtra(Constants.BUDGET, 150)

        dateTextView.text = displayDate
        timeTextView.text = "$displayStartTime - $displayEndTime"
        costTextView.text = "$$budget"

        generateItineraryItems(displayStartTime, displayEndTime, budget)
        convertToItineraryObject(displayDate, displayStartTime, displayEndTime, budget)
    }

    private fun setupAdapter() {
        val onItemClick: (Itinerary) -> Unit = { itinerary ->
            Toast.makeText(this, "Viewing details for: ${itinerary.title}", Toast.LENGTH_SHORT).show()
        }

        val onShareClick: (Itinerary, View) -> Unit = { itinerary, _ ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out my itinerary: ${itinerary.title} in ${itinerary.destination}\n" +
                            "From ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(itinerary.startDate)} " +
                            "to ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(itinerary.endDate)}"
                )
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share Itinerary"))
        }

        val onDeleteClick: (Itinerary) -> Unit = { itinerary ->
            AlertDialog.Builder(this)
                .setTitle("Delete Itinerary")
                .setMessage("Are you sure you want to delete ${itinerary.title}?")
                .setPositiveButton("Delete") { dialog, _ ->
                    Toast.makeText(this, "Itinerary deleted", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        itineraryAdapter = ItineraryAdapter(onItemClick, onShareClick, onDeleteClick)
        itineraryRecyclerView.adapter = itineraryAdapter

        currentItinerary?.let {
            itineraryAdapter.submitList(listOf(it))
        }
    }

    private fun generateItineraryItems(startTime: String, endTime: String, budget: Int) {
        itineraryItems.clear()

        val startTimeMillis = parseTimeToMillis(startTime)
        val endTimeMillis = parseTimeToMillis(endTime)
        val totalDurationMillis = endTimeMillis - startTimeMillis
        val totalHours = totalDurationMillis / (60 * 60 * 1000)

        var currentTimeMillis = startTimeMillis
        var remainingBudget = budget

        val activities = generateActivitiesByBudget(budget, totalHours.toInt())

        for (activity in activities) {
            val activityDuration = 2 // Assume 2 hours for demo
            val activityDurationMillis = activityDuration * 60 * 60 * 1000
            val endActivityTimeMillis = currentTimeMillis + activityDurationMillis

            itineraryItems.add(
                ActivityItem(
                    id = activity.id,
                    title = activity.title,
                    description = activity.description,
                    imageUrl = activity.imageUrl,
                    sectionId = 2,
                    startHour = "",
                    endHour = "",
                    city = "",
                    date = "",
                    type = "",
                    cost = activity.cost,
                    rating = 5f,
                    location = ""
                )
            )

            currentTimeMillis = endActivityTimeMillis
            remainingBudget -= activity.cost.toInt()

            if (activity != activities.last()) {
                currentTimeMillis += 30 * 60 * 1000 // 30 minutes travel
            }

            if (currentTimeMillis > endTimeMillis) break
        }
    }

    private fun convertToItineraryObject(
        displayDate: String,
        startTime: String,
        endTime: String,
        budget: Int
    ) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val startDate = try {
            dateFormat.parse(displayDate) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        val endDate = startDate

        val dayPlan = DayPlan(
            dayNumber = 1,
            date = startDate,
            activities = itineraryItems,
            title = "Day 1 in ${determineDestination()}"
        )

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

    private fun determineActivityType(title: String): ActivityType {
        return when {
            title.contains("Coffee", true) || title.contains("Lunch", true) ||
                    title.contains("Dinner", true) || title.contains("Food", true) -> ActivityType.RESTAURANT
            title.contains("Museum", true) || title.contains("Historical", true) ||
                    title.contains("Tour", true) || title.contains("Boat", true) -> ActivityType.ATTRACTION
            title.contains("Shopping", true) || title.contains("Market", true) -> ActivityType.SHOPPING
            else -> ActivityType.OTHER
        }
    }

    private fun determineDestination(): String {
        return "Local City"
    }

    private fun parseTimeToMillis(timeString: String): Long {
        return try {
            val format = SimpleDateFormat("h:mm a", Locale.getDefault())
            val date = format.parse(timeString)
            val calendar = Calendar.getInstance().apply {
                if (date != null) time = date
            }
            calendar.timeInMillis
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    private fun setupListeners() {
        backButton.setOnClickListener { finish() }

        viewCustomItineraryButton.setOnClickListener {
            Toast.makeText(this, "Custom itinerary clicked", Toast.LENGTH_SHORT).show()
        }

        downloadButton.setOnClickListener {
            Toast.makeText(this, "Download feature not implemented", Toast.LENGTH_SHORT).show()
        }

        editButton.setOnClickListener {
            Toast.makeText(this, "Edit itinerary clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateActivitiesByBudget(budget: Int, hours: Int): List<ActivityItem> {
        // Dummy implementation - replace with real logic
        return emptyList()
    }
}
