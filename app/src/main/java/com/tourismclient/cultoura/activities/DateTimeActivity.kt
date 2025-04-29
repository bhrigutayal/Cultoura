package com.tourismclient.cultoura.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimeActivity : AppCompatActivity() {

    private lateinit var dateInput: TextInputEditText
    private lateinit var startTimeInput: TextInputEditText
    private lateinit var endTimeInput: TextInputEditText
    private lateinit var backStepButton: Button
    private lateinit var continueButton: Button
    private lateinit var backButton: ImageButton

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedStartTime: Calendar = Calendar.getInstance()
    private var selectedEndTime: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_time)

        initViews()
        setupListeners()
        updateButtonState()
    }

    private fun initViews() {
        dateInput = findViewById(R.id.dateInput)
        startTimeInput = findViewById(R.id.startTimeInput)
        endTimeInput = findViewById(R.id.endTimeInput)
        backStepButton = findViewById(R.id.backStepButton)
        continueButton = findViewById(R.id.continueButton)
        backButton = findViewById(R.id.backButton)

        // Initialize end time to be 3 hours after start time
        selectedEndTime.add(Calendar.HOUR_OF_DAY, 3)
    }

    private fun setupListeners() {
        // Date picker dialog
        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        // Start time picker dialog
        startTimeInput.setOnClickListener {
            showTimePickerDialog(true)
        }

        // End time picker dialog
        endTimeInput.setOnClickListener {
            showTimePickerDialog(false)
        }

        // Navigation buttons
        backStepButton.setOnClickListener {
            finish() // Go back to previous activity
        }

        backButton.setOnClickListener {
            finish() // Go back to previous activity
        }

        continueButton.setOnClickListener {
            if (validateInputs()) {
                proceedToNextStep()
            }
        }
    }

    private fun showDatePickerDialog() {
        val year = selectedDate.get(Calendar.YEAR)
        val month = selectedDate.get(Calendar.MONTH)
        val day = selectedDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                updateDateField()
                updateButtonState()
            },
            year, month, day
        )

        // Set min date to today
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(isStartTime: Boolean) {
        val calendar = if (isStartTime) selectedStartTime else selectedEndTime
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                if (isStartTime) {
                    selectedStartTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                    selectedStartTime.set(Calendar.MINUTE, selectedMinute)
                    updateStartTimeField()

                    // Automatically update end time to be 3 hours after start time
                    selectedEndTime.set(Calendar.HOUR_OF_DAY, selectedHour + 3)
                    selectedEndTime.set(Calendar.MINUTE, selectedMinute)
                    updateEndTimeField()
                } else {
                    selectedEndTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                    selectedEndTime.set(Calendar.MINUTE, selectedMinute)
                    updateEndTimeField()
                }
                updateButtonState()
            },
            hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun updateDateField() {
        val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        dateInput.setText(dateFormat.format(selectedDate.time))
    }

    private fun updateStartTimeField() {
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        startTimeInput.setText(timeFormat.format(selectedStartTime.time))
    }

    private fun updateEndTimeField() {
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        endTimeInput.setText(timeFormat.format(selectedEndTime.time))
    }

    private fun validateInputs(): Boolean {
        // Check if date is selected
        if (dateInput.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if start time is selected
        if (startTimeInput.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select a start time", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if end time is selected
        if (endTimeInput.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select an end time", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if end time is after start time
        if (selectedEndTime.before(selectedStartTime)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun updateButtonState() {
        // Enable continue button only if all fields are filled
        continueButton.isEnabled = !dateInput.text.isNullOrEmpty() &&
                !startTimeInput.text.isNullOrEmpty() &&
                !endTimeInput.text.isNullOrEmpty() &&
                !selectedEndTime.before(selectedStartTime)

        // Update button color based on enabled state
        continueButton.backgroundTintList = ColorStateList.valueOf(
            if (continueButton.isEnabled)
                resources.getColor(R.color.black, theme)
            else
                resources.getColor(R.color.grey, theme)
        )
    }

    private fun proceedToNextStep() {
        // Get data from previous step
        val planType = intent.getStringExtra(Constants.PLAN_TYPE) ?: ""

        // Format date and times for next step
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val displayFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

        val formattedDate = dateFormat.format(selectedDate.time)
        val formattedStartTime = timeFormat.format(selectedStartTime.time)
        val formattedEndTime = timeFormat.format(selectedEndTime.time)

        val displayStartTime = displayFormat.format(selectedStartTime.time)
        val displayEndTime = displayFormat.format(selectedEndTime.time)

        // Create intent for budget activity
        val intent = Intent(this, BudgetActivity::class.java).apply {
            putExtra(Constants.PLAN_TYPE, planType)
            putExtra(Constants.DATE, formattedDate)
            putExtra("displayDate", dateInput.text.toString())
            putExtra(Constants.START_TIME, formattedStartTime)
            putExtra(Constants.END_TIME, formattedEndTime)
            putExtra("displayStartTime", displayStartTime)
            putExtra("displayEndTime", displayEndTime)
        }
        startActivity(intent)
    }
}