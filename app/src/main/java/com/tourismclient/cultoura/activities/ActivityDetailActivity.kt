package com.tourismclient.cultoura.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.adapters.ActivityDetailPagerAdapter
import com.tourismclient.cultoura.databinding.ActivityDescriptionActivityBinding
import com.tourismclient.cultoura.models.ActivityItem

class ActivityDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionActivityBinding
    private lateinit var detailAdapter: ActivityDetailPagerAdapter
    private var currentSectionId: Long = -1
    private var selectedActivityId: Long? = null
    private lateinit var activities: ArrayList<ActivityItem>
    private var initialPosition: Int = 0

    companion object {
        private const val EXTRA_SECTION_ID = "section_id"
        private const val EXTRA_ACTIVITIES = "activities"
        private const val EXTRA_SELECTED_ACTIVITY_ID = "selected_activity_id"
        private const val EXTRA_INITIAL_POSITION = "initial_position"

        fun createIntent(
            context: Context,
            sectionId: Long,
            activities: ArrayList<ActivityItem>,
            selectedActivityId: Long?,
            initialPosition: Int
        ): Intent {
            return Intent(context, ActivityDetailActivity::class.java).apply {
                putExtra(EXTRA_SECTION_ID, sectionId)
                putParcelableArrayListExtra(EXTRA_ACTIVITIES, activities)
                putExtra(EXTRA_SELECTED_ACTIVITY_ID, selectedActivityId)
                putExtra(EXTRA_INITIAL_POSITION, initialPosition)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        extractIntentData()
        setupViewPager()
        setupCloseButton()
    }

    private fun extractIntentData() {
        currentSectionId = intent.getLongExtra(EXTRA_SECTION_ID, -1)
        selectedActivityId = intent.getLongExtra(EXTRA_SELECTED_ACTIVITY_ID, -1L)
        if (selectedActivityId == -1L) selectedActivityId = null
        activities = intent.getParcelableArrayListExtra(EXTRA_ACTIVITIES) ?: arrayListOf()
        initialPosition = intent.getIntExtra(EXTRA_INITIAL_POSITION, 0)
    }

    private fun setupViewPager() {
        detailAdapter = ActivityDetailPagerAdapter(
            activities,
            selectedActivityId
        ) { activity ->
            // Handle activity selection
            val resultIntent = Intent().apply {
                putExtra(EXTRA_SECTION_ID, currentSectionId)
                putExtra("activity_id", activity.id)
                putExtra("activity", activity)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.activityDetailViewPager.apply {
            adapter = detailAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

            // Set current item AFTER setting the adapter
            post {
                currentItem = initialPosition
            }
        }
    }

    private fun setupCloseButton() {
        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        closeButton.setOnClickListener {
            finish()
        }
    }
}