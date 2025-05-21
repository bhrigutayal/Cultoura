package com.tourismclient.cultoura.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.models.ActivityItem

class ActivityDetailPagerAdapter(
    private val activities: List<ActivityItem>,
    private val selectedActivityId: Long?,
    private val context : Activity,
    private val onActivitySelected: (ActivityItem) -> Unit
) : RecyclerView.Adapter<ActivityDetailPagerAdapter.DetailPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_details_activity, parent, false)
        return DetailPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailPageViewHolder, position: Int) {
        val activity = activities[position]
        holder.bind(activity, activity.id == selectedActivityId)
    }

    override fun getItemCount(): Int = activities.size

    inner class DetailPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val activityImage: ImageView = itemView.findViewById(R.id.detailActivityImage)
        private val activityTitle: TextView = itemView.findViewById(R.id.detailActivityTitle)
        private val activityDescription: TextView = itemView.findViewById(R.id.detailActivityDescription)
        private val selectButton: Button = itemView.findViewById(R.id.selectButton)

        fun bind(activity: ActivityItem, isSelected: Boolean) {
            Glide
                .with(context)
                .load(activity.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_sample)
                .into(activityImage)

            activityTitle.text = activity.title
            activityDescription.text = activity.description

            if (isSelected) {
                selectButton.text = "SELECTED"
                selectButton.isEnabled = false
            } else {
                selectButton.text = "SELECT ACTIVITY"
                selectButton.isEnabled = true
            }

            selectButton.setOnClickListener {
                onActivitySelected(activity)
            }
        }
    }
}