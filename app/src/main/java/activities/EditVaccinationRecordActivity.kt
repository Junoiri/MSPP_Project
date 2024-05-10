package activities

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shawnlin.numberpicker.NumberPicker
import db.DConnection
import db.user.UserSF
import db.vaccination_record.VaccinationRecord
import db.vaccination_record.VaccinationRecordQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date

/**
 * This activity is responsible for editing vaccination records.
 * It provides functionalities: editing a vaccination record and updating it in the database.
 */
class EditVaccinationRecordActivity : AppCompatActivity() {

    private val vaccinationRecordQueries = VaccinationRecordQueries(DConnection.getConnection())

    /**
     * The email of the currently logged in user.
     */
    private val userEmail = Firebase.auth.currentUser?.email.toString()

    /**
     * Initializes the activity view and sets up the toolbar, date picker, number picker and save button.
     * It also populates the fields with the data from the vaccine variable.
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

        // Connect with database - display vaccination data
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val recordId = intent.getIntExtra("record_id", -1)
                val vaccinationRecord = getRecord(recordId)

                // Display fetched data
                vaccineName.setText(vaccinationRecord?.vaccine_name ?: "")
                manufacturer.setText(vaccinationRecord?.manufacturer ?: "")
                dateAdministeredButton.text =
                    "Date administered: ${vaccinationRecord?.date_administrated}"
                nextDoseDueDateButton.text =
                    "Next dose due date: ${vaccinationRecord?.next_dose_due_date}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
        titleTextView.text = "Edit Record"
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

            // Prepare vaccination record object
            CoroutineScope(Dispatchers.Main).launch {
                val userId = getId(userEmail)
                userId?.let {
                    // Prepare vaccination record object
                    val vaccinationRecord = VaccinationRecord(
                        vaccine_name = vaccineNameText,
                        manufacturer = manufacturerText,
                        date_administrated = Date.valueOf(
                            dateAdministeredText.substringAfter(":").trim()
                        ),
                        next_dose_due_date = Date.valueOf(
                            nextDoseDueDateText.substringAfter(":").trim()
                        ),
                        user_id = userId
                    )

                    // Update vaccination record
                    try {
                        val recordId = vaccinationRecord.record_id
                            ?: throw IllegalArgumentException("Record ID is null")
                        val recordUpdated = updateRecord(
                            recordId,
                            vaccinationRecord,
                            this@EditVaccinationRecordActivity
                        )
                        if (recordUpdated) {
                            Toast.makeText(
                                this@EditVaccinationRecordActivity,
                                "Vaccination record saved!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@EditVaccinationRecordActivity,
                                "Failed to save vaccination record",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@EditVaccinationRecordActivity,
                            "Failed to save vaccination record",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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
     * Retrieves the vaccination record from the database using the record ID.
     */
    private suspend fun getRecord(recordId: Int): VaccinationRecord? {
        return withContext(Dispatchers.IO) {
            vaccinationRecordQueries.getRecord(recordId)
        }
    }

    /**
     * Updates the vaccination record in the database.
     */
    private suspend fun updateRecord(
        recordId: Int,
        vaccinationRecord: VaccinationRecord,
        context: Context
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val result = vaccinationRecordQueries.updateRecord(recordId, vaccinationRecord)
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Record updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record update failed", Toast.LENGTH_SHORT).show()
                }
            }
            result
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
