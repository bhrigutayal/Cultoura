package com.tourismclient.cultoura.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.models.ActivityItem
import com.tourismclient.cultoura.models.ActivitySection

class ActivitySelectionAdapter(
    private val sections: List<ActivitySection>,
    private val onActivityClicked: (sectionId: Long, activityId: Long, position: Int) -> Unit,
    private val onActivitySelected: (sectionId: Long, activity: ActivityItem) -> Unit
) : RecyclerView.Adapter<ActivitySelectionAdapter.SectionViewHolder>() {

    // Track selected items for each section
    private val selectedActivities = mutableMapOf<Long, Long>() // sectionId to activityId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_activities, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.bind(section, selectedActivities[section.id])
    }

    override fun getItemCount(): Int = sections.size

    fun updateSelection(sectionId: Long, activityId: Long, shouldRemove: Boolean) {
        if (shouldRemove) {
            selectedActivities.remove(sectionId)
        } else {
            selectedActivities[sectionId] = activityId
        }
        notifyDataSetChanged()
    }

    inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sectionTitle: TextView = itemView.findViewById(R.id.sectionTitle)
        private val activitiesContainer: RecyclerView = itemView.findViewById(R.id.activitiesGridRecyclerView)

        fun bind(section: ActivitySection, selectedActivityId: Long?) {
            sectionTitle.text = section.title

            // Set up grid RecyclerView for activities in this section
            val activityAdapter = ActivityGridAdapter(
                section.activities,
                selectedActivityId,
                { activity, position ->
                    // Handle click to show full-screen detail
                    onActivityClicked(section.id, activity.id, position)
                },
                { activity ->
                    // Handle selection (this will be called from detail view)
                    onActivitySelected(section.id, activity)
                }
            )

            activitiesContainer.apply {
                layoutManager = GridLayoutManager(context, 3) // 3 items per row
                adapter = activityAdapter
            }
        }
    }
}