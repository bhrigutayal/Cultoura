package com.tourismclient.cultoura.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.models.ActivityItem

class ActivityGridAdapter(
    private val activities: List<ActivityItem>,
    private val selectedActivityId: Long?,
    private val onActivityClicked: (ActivityItem, Int) -> Unit,
    private val onActivitySelected: (ActivityItem) -> Unit
) : RecyclerView.Adapter<ActivityGridAdapter.GridItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_grid_layout, parent, false)
        return GridItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        val activity = activities[position]
        holder.bind(activity, activity.id == selectedActivityId, position)
    }

    override fun getItemCount(): Int = activities.size

    inner class GridItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val activitySquare: MaterialCardView = itemView.findViewById(R.id.activitySquare)
        private val activityThumbnail: ImageView = itemView.findViewById(R.id.activityThumbnail)
        private val activityName: TextView = itemView.findViewById(R.id.activityName)
        private val selectedIndicator: ImageView = itemView.findViewById(R.id.selectedIndicator)

        fun bind(activity: ActivityItem, isSelected: Boolean, position: Int) {
            activityName.text = activity.title
            activityThumbnail.setImageResource(activity.imageUrl)

            // Visual indication of selection
            if (isSelected) {
                selectedIndicator.visibility = View.VISIBLE
                activitySquare.strokeColor = itemView.context.getColor(R.color.black)
                activitySquare.strokeWidth = 2
            } else {
                selectedIndicator.visibility = View.GONE
                activitySquare.strokeWidth = 0
            }

            // Handle click to open detail view
            itemView.setOnClickListener {
                // Pass the exact position of this item
                onActivityClicked(activity, position)
            }
        }
    }
}