package com.tourismclient.cultoura.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import  com.tourismclient.cultoura.R
import  com.tourismclient.cultoura.models.Event
import com.bumptech.glide.Glide

class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_event)
        val imageView: ImageView = itemView.findViewById(R.id.image_event)
        val titleText: TextView = itemView.findViewById(R.id.text_event_title)
        val dateText: TextView = itemView.findViewById(R.id.text_event_date)
        val locationText: TextView = itemView.findViewById(R.id.text_event_location)
        val distanceText: TextView = itemView.findViewById(R.id.text_event_distance)
        val featuredBadge: View = itemView.findViewById(R.id.badge_featured)
        val featuredText : TextView = itemView.findViewById(R.id.text_featured)
        val saveButton: ImageView = itemView.findViewById(R.id.button_save_event)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.titleText.text = event.title
        holder.dateText.text = event.date
        holder.locationText.text = event.location
        holder.distanceText.text = event.distance

        // Load image using Glide
        Glide.with(holder.imageView.context)
            .load(event.imageUrl)
            .placeholder(R.drawable.ic_sample)
            .into(holder.imageView)

        // Show/hide featured badge
        holder.featuredBadge.visibility = if (event.isFeatured) View.VISIBLE else View.GONE
        holder.featuredText.visibility = if (event.isFeatured) View.VISIBLE else View.GONE

        // Set save button state
        holder.saveButton.setImageResource(
            if (event.isSaved) R.drawable.ic_saved
            else R.drawable.ic_saved
        )

        // Set click listener for save button
        holder.saveButton.setOnClickListener {
            event.isSaved = !event.isSaved
            holder.saveButton.setImageResource(
                if (event.isSaved) R.drawable.ic_saved
                else R.drawable.ic_saved
            )
            // In a real app, you would save this state to a database
        }

        // Set click listener for card
        holder.cardView.setOnClickListener {
            // Navigate to event details (in a real app)
        }
    }

    override fun getItemCount() = events.size
}