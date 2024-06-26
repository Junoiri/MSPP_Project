package activities

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.shawnlin.numberpicker.NumberPicker
import db.scheduled_vaccination.ScheduledVaccination
import db.scheduled_vaccination.ScheduledVaccinationSF
import db.user.UserSF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date

/**
 * This activity is responsible for editing upcoming vaccination records.
 * It provides functionalities: editing a vaccination record and updating it in the database.
 */
class EditUpcomingVaccinationActivity : AppCompatActivity() {

    /**
     * The email of the currently logged in user.
     */
    private var userEmail = Firebase.auth.currentUser?.email.toString()

    /**
     * Initializes the activity view and sets up the toolbar, date picker, number picker and save button.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_upcoming_vaccination)

        setupToolbar()
        setupDatePicker()
        setupNumberPicker()
        setupSaveButton()

        // Populate the fields with the data from the vaccine variable
        val vaccineName: EditText = findViewById(R.id.vaccine_name)
        val manufacturer: EditText = findViewById(R.id.manufacturer)
        val dateAdministeredButton: Button = findViewById(R.id.date_administrated)
        val nextDoseDueDateButton: Button = findViewById(R.id.next_dose_due_date)


    }

    /**
     * Sets up the toolbar with a back button and a title.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<LinearLayout>(R.id.toolbar)

        // Find the back button from the included toolbar layout and set its click listener
        val backButton: ImageView = toolbar.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val titleTextView: TextView = toolbar.findViewById(R.id.toolbar_title)
        titleTextView.text = "Edit Schedule"
    }

    /**
     * Sets up the date picker for the date administered and the next dose due date.
     */
    private fun setupDatePicker() {
        val dateAdministeredButton: Button = findViewById(R.id.date_administrated)
        dateAdministeredButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                // Display the selected date on the button
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateAdministeredButton.text = "Date administered: $selectedDate"
            }
            datePickerDialog.show()
        }

        val nextDoseDueDateButton: Button = findViewById(R.id.next_dose_due_date)
        nextDoseDueDateButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                // Display the selected date on the button
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                nextDoseDueDateButton.text = "Next dose due date: $selectedDate"
            }
            datePickerDialog.show()
        }
    }

    /**
     * Sets up the number picker for the total doses and doses taken.
     */
    private fun setupNumberPicker() {
        val totalDosesNumberPicker: NumberPicker = findViewById(R.id.dose_total_number_picker)
        val dosesTakenNumberPicker: NumberPicker = findViewById(R.id.dose_taken_number_picker)

        // Initially, disable the second number picker
        dosesTakenNumberPicker.isEnabled = false

        // Setup the first number picker
        totalDosesNumberPicker.maxValue = 9
        totalDosesNumberPicker.minValue = 1
        totalDosesNumberPicker.value = 1
        totalDosesNumberPicker.isFadingEdgeEnabled = true
        totalDosesNumberPicker.isScrollerEnabled = true
        totalDosesNumberPicker.wrapSelectorWheel = true
        totalDosesNumberPicker.isAccessibilityDescriptionEnabled = true

        // When the value of the first number picker changes, update the maximum value of the second number picker and enable it
        totalDosesNumberPicker.setOnValueChangedListener { _, _, newVal ->
            dosesTakenNumberPicker.maxValue = newVal
            dosesTakenNumberPicker.isEnabled = true
        }

        // Setup the second number picker
        dosesTakenNumberPicker.minValue = 1
        dosesTakenNumberPicker.value = 1
        dosesTakenNumberPicker.isFadingEdgeEnabled = true
        dosesTakenNumberPicker.isScrollerEnabled = true
        dosesTakenNumberPicker.wrapSelectorWheel = true
        dosesTakenNumberPicker.isAccessibilityDescriptionEnabled = true
    }

    /**
     * Sets up the save button to validate the input fields and update the vaccination record in the database.
     */
    private fun setupSaveButton() {
        val saveButton: FloatingActionButton = findViewById(R.id.save_vaccination)
        val vaccineName: EditText = findViewById(R.id.vaccine_name)
        val manufacturer: EditText = findViewById(R.id.manufacturer)
        val dateAdministeredButton: Button = findViewById(R.id.date_administrated)
        val nextDoseDueDateButton: Button = findViewById(R.id.next_dose_due_date)

        saveButton.setOnClickListener {
            val vaccineNameText = vaccineName.text.toString()
            val manufacturerText = manufacturer.text.toString()
            val dateAdministeredText = dateAdministeredButton.text.toString()
            val nextDoseDueDateText = nextDoseDueDateButton.text.toString()

            if (vaccineNameText.isEmpty()) {
                Toast.makeText(this, "Vaccine name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (manufacturerText.isEmpty()) {
                Toast.makeText(this, "Manufacturer cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dateAdministeredText == "Select Date Administered") {
                Toast.makeText(this, "Please select a date administered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nextDoseDueDateText == "Select Next Dose Due Date") {
                Toast.makeText(this, "Please select the next dose due date", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val scheduleId = intent.getIntExtra("scheduleId", -1)
            if (scheduleId != -1) {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val userId = getId(userEmail)
                        userId?.let {
                            val scheduledVaccination = ScheduledVaccination(
                                schedule_id = scheduleId,
                                vaccine_name = vaccineNameText,
                                schedule_date = Date.valueOf(dateAdministeredText),
                                manufacturer = manufacturerText,
                                dose = nextDoseDueDateText,
                                user_id = userId
                            )

                            updateScheduledVaccination(
                                scheduleId,
                                scheduledVaccination,
                                applicationContext
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditUpcomingVaccinationActivity,
                            "Failed to get user ID",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("EditUpcomingVaccination", "Error: ${e.message}")
                    }
                }
            } else {
                Toast.makeText(this, "Invalid Schedule ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Retrieves the user ID from the database using the user's email.
     */
    private suspend fun getId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            UserSF.getId(email)
        }
    }

    /**
     * Updates the scheduled vaccination record in the database.
     */
    private suspend fun updateScheduledVaccination(
        schedule_id: Int,
        scheduledVaccination: ScheduledVaccination,
        context: Context
    ) {
        withContext(Dispatchers.IO) {
            val result =
                ScheduledVaccinationSF.updateScheduledVaccination(schedule_id, scheduledVaccination)
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Schedule updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Schedule update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Overrides the back button press to show a Snackbar asking the user to confirm their intention to quit.
     * If the user confirms, the activity is finished.
     */
    override fun onBackPressed() {
        val snackbar = Snackbar.make(
            findViewById(R.id.coordinator_layout),
            "Do you want to quit? Your progress won't be saved",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Yes") {
            super.onBackPressed()
        }
        snackbar.show()

        // Dismiss the Snackbar when the user touches outside of it
        findViewById<androidx.coordinatorlayout.widget.CoordinatorLayout>(R.id.coordinator_layout).setOnTouchListener { _, _ ->
            snackbar.dismiss()
            false
        }
    }
}
