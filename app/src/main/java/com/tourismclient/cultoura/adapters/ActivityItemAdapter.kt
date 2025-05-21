package com.tourismclient.cultoura.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.models.ActivityItem

class ActivityItemAdapter(
    private val activities: List<ActivityItem>,
    private val selectedActivityId: Long?,
    private val context : Activity,
    private val onActivityClick: (ActivityItem) -> Unit
) : RecyclerView.Adapter<ActivityItemAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.bind(activity, activity.id == selectedActivityId)
    }

    override fun getItemCount(): Int = activities.size

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val activityContainer: MaterialCardView = itemView.findViewById(R.id.activityContainer)
        private val activityImage: ImageView = itemView.findViewById(R.id.activityImage)
        private val activityTitle: TextView = itemView.findViewById(R.id.activityTitle)
        private val activityDescription: TextView = itemView.findViewById(R.id.activityDescription)
        private val activitySelectButton : Button = itemView.findViewById(R.id.selectActivityButton)


        fun bind(activity: ActivityItem, isSelected: Boolean) {
            activityTitle.text = activity.title
            activityDescription.text = activity.description
            Glide
                .with(context)
                .load(activity.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_sample)
                .into(activityImage)

            // Visual indication of selection
            // Replace CardView with MaterialCardView in your layout XML file
// Then the stroke properties will work as expected

            if (isSelected) {
                activitySelectButton.setBackgroundColor(itemView.context.getColor(R.color.black))
                activitySelectButton.text = "SELECTED"
                activitySelectButton.setTextColor(Color.WHITE)
                activityContainer.strokeColor = itemView.context.getColor(R.color.black)
                activityContainer.strokeWidth = 2.dpToPx(itemView.context)
            } else {
                activitySelectButton.setBackgroundColor(itemView.context.getColor(R.color.grey))
                activitySelectButton.text = "SELECT ACTIVITY"
                activitySelectButton.setTextColor(Color.BLACK)
                activityContainer.strokeWidth = 0
            }

            activitySelectButton.setOnClickListener {
                onActivityClick(activity)
            }
        }
    }


    // Extension function to convert dp to pixels
    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}