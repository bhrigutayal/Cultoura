package com.tourismclient.cultoura.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.databinding.ActivityBudgetBinding
import com.tourismclient.cultoura.utils.Constants

class BudgetActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBudgetBinding

    private val MIN_BUDGET = 0
    private val MAX_BUDGET = 1000
    private val DEFAULT_BUDGET = 150.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        updateBudgetSuggestion(DEFAULT_BUDGET)
    }


    private fun setupListeners() {
        // Sync SeekBar with EditText
        binding.budgetSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.budgetEditText.setText(progress.toString())
                    updateBudgetSuggestion(progress.toDouble())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Sync EditText with SeekBar
        binding.budgetEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val budgetInput = s.toString()
                if (budgetInput.isNotEmpty()) {
                    try {
                        val budget = budgetInput.toInt()
                        if (budget in MIN_BUDGET..MAX_BUDGET) {
                            binding.budgetSeekBar.progress = budget
                            updateBudgetSuggestion(budget.toDouble())
                        }
                    } catch (e: NumberFormatException) {
                        // Handle invalid input
                    }
                }
            }
        })

        binding.backButton.setOnClickListener {
            finish() // Go back to previous activity
        }

        binding.createItineraryButton.setOnClickListener {
            val budget = binding.budgetEditText.text.toString().toDoubleOrNull() ?: DEFAULT_BUDGET
            proceedToItinerary(budget)
        }
    }

    private fun updateBudgetSuggestion(budget: Double) {
        binding.budgetSuggestionTextView.text = "We'll suggest activities that fit within your budget of ₹${budget}."
    }

    private fun proceedToItinerary(budget: Double) {
        // Get data from previous steps
        val planType = intent.getStringExtra(Constants.PLAN_TYPE) ?: ""
        val date = intent.getStringExtra(Constants.DATE) ?: ""
        val startTime = intent.getStringExtra(Constants.START_TIME) ?: ""
        val endTime = intent.getStringExtra(Constants.END_TIME) ?: ""

        // Create intent for final itinerary
        val intent = Intent(this, SelectActivityActivity::class.java).apply {
            putExtra(Constants.PLAN_TYPE, planType)
            putExtra(Constants.DATE, date)
            putExtra(Constants.START_TIME, startTime)
            putExtra(Constants.END_TIME, endTime)
            putExtra(Constants.BUDGET, budget)
        }
        startActivity(intent)
    }

    // Extension function to format numbers with commas
    private fun Int.toFormattedString(): String {
        return String.format("%,d", this)
    }
}