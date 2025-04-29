package com.tourismclient.cultoura.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.adapters.EventAdapter
import com.tourismclient.cultoura.models.Event

class SavedEventsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var emptyView: TextView
    private val eventsList = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_events, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_saved_events)
        emptyView = view.findViewById(R.id.text_no_saved_events)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapter
        eventAdapter = EventAdapter(eventsList)
        recyclerView.adapter = eventAdapter

        // Load saved events
        loadSavedEvents()

        return view
    }

    private fun loadSavedEvents() {
        // In a real app, you would fetch saved events from local database
        eventsList.clear()

        // For demo purposes, let's add some saved events
        // In a real app, this might be empty until user saves events
        eventsList.addAll(
            listOf(
                Event(
                    id = 5,
                    title = "Summer Concert Series",
                    description = "Featuring top artists in an outdoor venue",
                    date = "Saturday at 8:00 PM",
                    location = "Riverside Amphitheater",
                    distance = "7.1 miles",
                    imageUrl = "https://example.com/concert.jpg",
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

        // Show/hide empty view based on data
        if (eventsList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            eventAdapter.notifyDataSetChanged()
        }
    }
}
