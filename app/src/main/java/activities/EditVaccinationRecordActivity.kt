package activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shawnlin.numberpicker.NumberPicker
import com.google.android.material.snackbar.Snackbar

class EditVaccinationRecordActivity : AppCompatActivity() {
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

        // TODO: Connect with database - display vaccination data
        // vaccineName.setText(vaccine.name)
        // manufacturer.setText(vaccine.manufacturer)
        // dateAdministeredButton.text = "Date administered: ${vaccine.dateAdministered}"
        // nextDoseDueDateButton.text = "Next dose due date: ${vaccine.nextDoseDueDate}"
    }

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

    private fun setupDatePicker() {
        val dateAdministeredButton: Button = findViewById(R.id.date_administrated)
        dateAdministeredButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                // Display the selected date on the button
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateAdministeredButton.text = "Date administered: $selectedDate"
            }
            datePickerDialog.show()
        }

        val nextDoseDueDateButton: Button = findViewById(R.id.next_dose_due_date)
        nextDoseDueDateButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                // Display the selected date on the button
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                nextDoseDueDateButton.text = "Next dose due date: $selectedDate"
            }
            datePickerDialog.show()
        }
    }

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
        totalDosesNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
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
                Toast.makeText(this, "Please select the next dose due date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Vaccination saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

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