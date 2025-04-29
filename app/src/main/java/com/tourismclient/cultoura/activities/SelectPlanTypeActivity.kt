package com.tourismclient.cultoura.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.utils.Constants

class SelectPlanTypeActivity : AppCompatActivity() {

    private lateinit var dateOption: CardView
    private lateinit var friendsOption: CardView
    private lateinit var familyOption: CardView
    private lateinit var vacationOption: CardView
    private lateinit var continueButton: Button

    private var selectedOption: PlanType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_plan_type)

        setupToolbar()
        setupViews()
        setupListeners()
    }

    private fun setupToolbar() {
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val menuButton = findViewById<ImageButton>(R.id.menuButton)

        backButton.setOnClickListener {
            onBackPressed()
        }

        menuButton.setOnClickListener {
            // Show menu options
            // This could be implemented as a popup menu
        }
    }

    private fun setupViews() {
        dateOption = findViewById(R.id.dateOption)
        friendsOption = findViewById(R.id.friendsOption)
        familyOption = findViewById(R.id.familyOption)
        vacationOption = findViewById(R.id.vacationOption)
        continueButton = findViewById(R.id.continueButton)

        // Initially disable continue button
        updateContinueButtonState()
    }

    private fun setupListeners() {
        // Set up option selection listeners
        dateOption.setOnClickListener {
            selectOption(PlanType.DATE)
        }

        friendsOption.setOnClickListener {
            selectOption(PlanType.FRIENDS)
        }

        familyOption.setOnClickListener {
            selectOption(PlanType.FAMILY)
        }

        vacationOption.setOnClickListener {
            selectOption(PlanType.VACATION)
        }

        continueButton.setOnClickListener {
            if (selectedOption != null) {
                navigateToNextStep()
            }
        }
    }

    private fun selectOption(option: PlanType) {
        // Update selected option
        selectedOption = option

        // Reset all options to unselected state
        resetOptionSelections()

        // Highlight the selected option
        when (option) {
            PlanType.DATE -> highlightOption(dateOption)
            PlanType.FRIENDS -> highlightOption(friendsOption)
            PlanType.FAMILY -> highlightOption(familyOption)
            PlanType.VACATION -> highlightOption(vacationOption)
        }

        // Update continue button state
        updateContinueButtonState()
    }

    private fun resetOptionSelections() {
        dateOption.setCardBackgroundColor(getColor(R.color.white))
        friendsOption.setCardBackgroundColor(getColor(R.color.white))
        familyOption.setCardBackgroundColor(getColor(R.color.white))
        vacationOption.setCardBackgroundColor(getColor(R.color.white))

        // Reset elevation if needed
        dateOption.cardElevation = 0f
        friendsOption.cardElevation = 0f
        familyOption.cardElevation = 0f
        vacationOption.cardElevation = 0f
    }

    private fun highlightOption(cardView: CardView) {
        // Add visual indication of selection
        cardView.setCardBackgroundColor(getColor(R.color.selected_background))
        cardView.cardElevation = 4f
    }

    private fun updateContinueButtonState() {
        val isOptionSelected = selectedOption != null

        continueButton.isEnabled = isOptionSelected

        // Update visual state of button
        if (isOptionSelected) {
            continueButton.setBackgroundColor(getColor(R.color.black))
        } else {
            continueButton.setBackgroundColor(getColor(R.color.grey))
        }
    }

    private fun navigateToNextStep() {
        // Navigate to the next step in the planning process
        val intent = Intent(this, DateTimeActivity::class.java).apply {
            putExtra(Constants.PLAN_TYPE, selectedOption?.name)
        }
        startActivity(intent)
    }
}

// Enum to represent different plan types
enum class PlanType {
    DATE,
    FRIENDS,
    FAMILY,
    VACATION
}