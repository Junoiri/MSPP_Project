package activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mspp_project.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shawnlin.numberpicker.NumberPicker

class UpcomingVaccinationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_schedule_layout, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.let {
            setupDatePicker(it)
            setupNumberPicker(it)
            setupSaveButton(it)
        }
    }


    private fun setupDatePicker(view: View) {
        val dateScheduleButton: Button = view.findViewById(R.id.date_schedule)
        dateScheduleButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                // Display the selected date on the button
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateScheduleButton.text = "Date schedule: $selectedDate"
            }
            datePickerDialog.show()
        }
    }

    private fun setupNumberPicker(view: View) {
        val totalDosesNumberPicker: NumberPicker = view.findViewById(R.id.dose_total_number_picker)
        val dosesTakenNumberPicker: NumberPicker = view.findViewById(R.id.dose_taken_number_picker)

        // Initially, disable the second number picker
        dosesTakenNumberPicker.isEnabled = false

        // Setup the first number picker
        totalDosesNumberPicker.maxValue = 9
        totalDosesNumberPicker.minValue = 1
        totalDosesNumberPicker.value = 1

        // When the value of the first number picker changes, update the maximum value of the second number picker and enable it
        totalDosesNumberPicker.setOnValueChangedListener { _, _, newVal ->
            dosesTakenNumberPicker.maxValue = newVal
            dosesTakenNumberPicker.isEnabled = true
        }

        // Setup the second number picker
        dosesTakenNumberPicker.minValue = 1
        dosesTakenNumberPicker.value = 1
    }
   private fun setupSaveButton(view: View) {
    val saveButton: FloatingActionButton = view.findViewById(R.id.save_vaccination)
    val vaccineName: EditText = view.findViewById(R.id.vaccine_name)
    val manufacturer: EditText = view.findViewById(R.id.manufacturer)
    val scheduleDateButton: Button = view.findViewById(R.id.date_schedule)
    val totalDosesNumberPicker: NumberPicker = view.findViewById(R.id.dose_total_number_picker)
    val dosesTakenNumberPicker: NumberPicker = view.findViewById(R.id.dose_taken_number_picker)

    saveButton.setOnClickListener {
        val vaccineNameText = vaccineName.text.toString()
        val manufacturerText = manufacturer.text.toString()
        val scheduleDateText = scheduleDateButton.text.toString()

        if (vaccineNameText.isEmpty()) {
            Toast.makeText(requireContext(), "Vaccine name cannot be empty", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (manufacturerText.isEmpty()) {
            Toast.makeText(requireContext(), "Manufacturer cannot be empty", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (scheduleDateText == "Date schedule") {
            Toast.makeText(requireContext(), "Please select a schedule date", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (totalDosesNumberPicker.value == 0 || dosesTakenNumberPicker.value == 0) {
            Toast.makeText(requireContext(), "Please select the number of doses", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        Toast.makeText(requireContext(), "Vaccination schedule saved!", Toast.LENGTH_SHORT).show()
    }
}
}