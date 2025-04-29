package com.tourismclient.cultoura.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.databinding.ItemItineraryBinding
import com.tourismclient.cultoura.models.Itinerary

class ItineraryAdapter(
    private val onItemClick: (Itinerary) -> Unit,
    private val onShareClick: (Itinerary, View) -> Unit,
    private val onDeleteClick: (Itinerary) -> Unit
) : ListAdapter<Itinerary, ItineraryAdapter.ItineraryViewHolder>(ItineraryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder {
        val binding = ItemItineraryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItineraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItineraryViewHolder(
        private val binding: ItemItineraryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                expandOrCollapseDetails()
            }

            binding.btnShare.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onShareClick(getItem(position), binding.root)
                }
            }

            binding.btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(position))
                }
            }
        }

        fun bind(itinerary: Itinerary) {
            binding.apply {
                tvItineraryTitle.text = itinerary.title
                tvDestination.text = itinerary.destination
                tvDates.text = "${itinerary.startDate} - ${itinerary.endDate}"

                // Setup the itinerary days
                setupItineraryDays(itinerary)

                // Setup cover image if available
                if (itinerary.coverImagePath!!.isNotEmpty()) {
                    // Use your preferred image loading library
                    // Glide.with(itemView).load(itinerary.coverImageUrl).into(ivCoverImage)
                    ivCoverImage.visibility = View.VISIBLE
                } else {
                    ivCoverImage.visibility = View.GONE
                }
            }
        }

        private fun setupItineraryDays(itinerary: Itinerary) {
            binding.itineraryDaysContainer.removeAllViews()

            for (day in itinerary.dayPlans) {
                val dayView = LayoutInflater.from(binding.root.context)
                    .inflate(R.layout.item_itinerary_day, binding.itineraryDaysContainer, false)

                // Bind day data to view
                val tvDayTitle = dayView.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_day_title)
                val activitiesContainer = dayView.findViewById<ViewGroup>(R.id.activities_container)

                tvDayTitle.text = "Day ${day.dayNumber}: ${day.title}"

                // Add activities for this day
                for (activity in day.activities) {
                    val activityView = LayoutInflater.from(binding.root.context)
                        .inflate(R.layout.item_activity, activitiesContainer, false)

                    val tvTime = activityView.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_time)
                    val tvActivity = activityView.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_activity)
                    val tvLocation = activityView.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_location)

                    tvTime.text = activity.duration.toString()
                    tvActivity.text = activity.title
                    //tvLocation.text = activity.location.toString()

                    activitiesContainer.addView(activityView)
                }

                binding.itineraryDaysContainer.addView(dayView)
            }
        }

        private fun expandOrCollapseDetails() {
            val isExpanded = binding.itineraryDetailsContainer.visibility == View.VISIBLE

            if (isExpanded) {
                binding.itineraryDetailsContainer.visibility = View.GONE
                binding.expandIcon.setImageResource(R.drawable.ic_expand_more)
            } else {
                binding.itineraryDetailsContainer.visibility = View.VISIBLE
                binding.expandIcon.setImageResource(R.drawable.ic_expand_less)
                onItemClick(getItem(adapterPosition))
            }
        }
    }

    class ItineraryDiffCallback : DiffUtil.ItemCallback<Itinerary>() {
        override fun areItemsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
            return oldItem == newItem
        }
    }
}