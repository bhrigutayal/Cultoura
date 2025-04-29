package com.tourismclient.cultoura.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.adapters.EventAdapter
import com.tourismclient.cultoura.databinding.FragmentEventsListBinding
import com.tourismclient.cultoura.models.Event

class NearbyEventsFragment : Fragment() {

   // private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private val eventsList = mutableListOf<Event>()
    private lateinit var binding : FragmentEventsListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsListBinding.inflate(layoutInflater)
        eventAdapter = EventAdapter(eventsList)


        // Load nearby events data
        loadNearbyEvents()
        setUpRecyclerViews()

        return binding.root
    }

    private fun setUpRecyclerViews() {
        val recyclerViewFeatured = binding.recyclerViewFeatured
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFeatured.layoutManager = layoutManager
        recyclerViewFeatured.adapter = eventAdapter

        val recyclerViewNearby = binding.recyclerViewNearby
        val layoutManagerNearby = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNearby.layoutManager = layoutManagerNearby
        recyclerViewNearby.adapter = eventAdapter
        val activityType1 = binding.activityType1.root
        val activityType2 = binding.activityType2.root
        val activityType3 = binding.activityType3.root
        val activityType4 = binding.activityType4.root

        // Set up each activity type
        setupActivityType(activityType1, R.drawable.ic_take_off_plane, "Adventure")
        setupActivityType(activityType2, R.drawable.ic_clock, "Entertainment")
        setupActivityType(activityType3, R.drawable.ic_people, "Relaxing")
        setupActivityType(activityType4, R.drawable.ic_home, "Family Time")
    }

    private fun setupActivityType(itemView: View, iconResId: Int, name: String) {
        val icon = itemView.findViewById<ImageView>(R.id.activity_icon)
        val nameText = itemView.findViewById<TextView>(R.id.activity_name)

        icon.setImageResource(iconResId)
        nameText.text = name

        // Set click listener if needed
        itemView.setOnClickListener {
            // Handle click action
        }
    }

    private fun loadNearbyEvents() {
        // In a real app, you would fetch data from API or database
        // For now, let's add some dummy data
        eventsList.clear()
        eventsList.addAll(
            listOf(
                Event(
                    id = 1,
                    title = "Local Music Festival",
                    description = "Live bands and food stalls just 2 miles away",
                    date = "Today at 6:00 PM",
                    location = "Central Park",
                    distance = "1.8 miles",
                    imageUrl = "https://example.com/music_festival.jpg",
                    isFeatured = false
                ),
                Event(
                    id = 2,
                    title = "Farmers Market",
                    description = "Fresh local produce and handmade crafts",
                    date = "Tomorrow at 9:00 AM",
                    location = "Downtown Square",
                    distance = "0.5 miles",
                    imageUrl = "https://example.com/farmers_market.jpg",
                    isFeatured = true
                ),
                Event(
                    id = 3,
                    title = "Tech Meetup",
                    description = "Networking event for tech professionals",
                    date = "Thursday at 7:00 PM",
                    location = "Innovation Hub",
                    distance = "3.2 miles",
                    imageUrl = "https://example.com/tech_meetup.jpg",
                    isFeatured = false
                )
            )
        )
        eventAdapter.notifyDataSetChanged()
    }
}