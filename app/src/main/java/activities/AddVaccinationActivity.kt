package activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import android.graphics.Typeface
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shawnlin.numberpicker.NumberPicker
import com.google.android.material.snackbar.Snackbar


//TODO: input validation, save floating button, after selecting date, display it in the button
class AddVaccinationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccination)

        setupToolbar()
        setupDatePicker()
        setupSaveButton()

    }

    private fun setupToolbar() {
        val toolbar = findViewById<LinearLayout>(R.id.toolbar)

        // Find the back button from the included toolbar layout and set its click listener
        val backButton: ImageView = toolbar.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val titleTextView: TextView = toolbar.findViewById(R.id.toolbar_title)
        titleTextView.text = "Add Vaccination"
    }

    private fun setupDatePicker() {
    val scheduleDateButton: Button = findViewById(R.id.schedule_date)
    scheduleDateButton.setOnClickListener {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            // Display the selected date on the button
            val selectedDate = "$dayOfMonth/${month+1}/$year"
            scheduleDateButton.text = "Schedule date: $selectedDate"
        }
        datePickerDialog.show()
    }

    val nextDoseDueDateButton: Button = findViewById(R.id.next_dose_due_date)
    nextDoseDueDateButton.setOnClickListener {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            // Display the selected date on the button
            val selectedDate = "$dayOfMonth/${month+1}/$year"
            nextDoseDueDateButton.text = "Next dose due date: $selectedDate"
        }
        datePickerDialog.show()
    }
}
    private fun setupNumberPicker() {
        val numberPicker: NumberPicker = findViewById(R.id.number_picker)

        // Set divider color
//        numberPicker.dividerColor = ContextCompat.getColor(this, R.color.colorPrimary)

        // Set formatter
//        numberPicker.setFormatter(getString(R.string.number_picker_formatter))

        // Set selected text color
//        numberPicker.selectedTextColor = ContextCompat.getColor(this, R.color.colorPrimary)

        // Set selected text size
//        numberPicker.selectedTextSize = resources.getDimension(R.dimen.selected_text_size)

        // Set selected typeface
//        numberPicker.selectedTypeface = Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL)

        // Set text color
//        numberPicker.textColor = ContextCompat.getColor(this, R.color.dark_grey)

        // Set text size
//        numberPicker.textSize = resources.getDimension(R.dimen.text_size)

        // Set typeface
//        numberPicker.typeface = Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL)

        // Set value
        numberPicker.maxValue = 9
        numberPicker.minValue = 1
        numberPicker.value = 1

        // Set fading edge enabled
        numberPicker.isFadingEdgeEnabled = true

        // Set scroller enabled
        numberPicker.isScrollerEnabled = true

        // Set wrap selector wheel
        numberPicker.wrapSelectorWheel = true

        // Set accessibility description enabled
        numberPicker.isAccessibilityDescriptionEnabled = true

        // OnClickListener
        numberPicker.setOnClickListener {
            Log.d("NumberPicker", "Click on current value")
        }

        // OnValueChangeListener
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("NumberPicker", "oldVal: $oldVal, newVal: $newVal")
        }

        // OnScrollListener
        numberPicker.setOnScrollListener { picker, scrollState ->
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                Log.d("NumberPicker", "newVal: ${picker.value}")
            }
        }
    }

    private fun setupSaveButton() {
        val saveButton: FloatingActionButton = findViewById(R.id.save_vaccination)
        saveButton.setOnClickListener {
            Toast.makeText(this, "Vaccination saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    override fun onBackPressed() {
    val snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Do you want to quit? Your progress won't be saved", Snackbar.LENGTH_INDEFINITE)
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