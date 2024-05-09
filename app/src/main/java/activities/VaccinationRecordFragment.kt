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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.shawnlin.numberpicker.NumberPicker
import db.user.UserSF
import db.vaccination_record.VaccinationRecord
import db.vaccination_record.VaccinationRecordSF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VaccinationRecordFragment : Fragment() {
    private var userEmail = Firebase.auth.currentUser?.email.toString()
    private lateinit var dateAdministeredButton: Button
    private lateinit var nextDoseDueDateButton: Button
    private lateinit var saveButton: FloatingActionButton
    private lateinit var vaccineName: EditText
    private lateinit var manufacturer: EditText
    private lateinit var totalDosesNumberPicker: NumberPicker
    private lateinit var dosesTakenNumberPicker: NumberPicker
    private lateinit var selectedDateAdministered: String
    private lateinit var selectedNextDoseDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_record_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupDatePicker()
        setupNumberPicker()
        setupSaveButton()
    }

    private fun initializeViews(view: View) {
        dateAdministeredButton = view.findViewById(R.id.date_administrated)
        nextDoseDueDateButton = view.findViewById(R.id.next_dose_due_date)
        saveButton = view.findViewById(R.id.save_vaccination)
        vaccineName = view.findViewById(R.id.vaccine_name)
        manufacturer = view.findViewById(R.id.manufacturer)
        totalDosesNumberPicker = view.findViewById(R.id.dose_total_number_picker)
        dosesTakenNumberPicker = view.findViewById(R.id.dose_taken_number_picker)
    }

    private fun setupDatePicker() {
        dateAdministeredButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // Display the selected date on the button
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    calendar.set(year, month, dayOfMonth)
                    selectedDateAdministered = sdf.format(calendar.time)
                    dateAdministeredButton.text = "Date administered: $selectedDateAdministered"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        nextDoseDueDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // Display the selected date on the button
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    calendar.set(year, month, dayOfMonth)
                    selectedNextDoseDate = sdf.format(calendar.time)
                    nextDoseDueDateButton.text = "Next dose due date: $selectedNextDoseDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    private fun setupNumberPicker() {
        val totalDosesNumberPicker: NumberPicker = view?.findViewById(R.id.dose_total_number_picker)!!
        val dosesTakenNumberPicker: NumberPicker = view?.findViewById(R.id.dose_taken_number_picker)!!

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

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val vaccineNameText = vaccineName.text.toString()
            val manufacturerText = manufacturer.text.toString()
            val dateAdministeredText = dateAdministeredButton.text.toString()
            val nextDoseDueDateText = nextDoseDueDateButton.text.toString()

            if (vaccineNameText.isEmpty()) {
                Toast.makeText(requireContext(), "Vaccine name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (manufacturerText.isEmpty()) {
                Toast.makeText(requireContext(), "Manufacturer cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dateAdministeredText == "Date administered") {
                Toast.makeText(requireContext(), "Please select a date administered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nextDoseDueDateText == "Next dose due date") {
                Toast.makeText(requireContext(), "Please select the next dose due date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (totalDosesNumberPicker.value == 0 || dosesTakenNumberPicker.value == 0) {
                Toast.makeText(requireContext(), "Please select the number of doses", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totalDoses = totalDosesNumberPicker.value
            val dosesTaken = dosesTakenNumberPicker.value

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateAdministered = try {
                Date(sdf.parse(dateAdministeredText)?.time ?: 0)
            } catch (e: Exception) {
                null
            }

            val nextDoseDueDate = try {
                Date(sdf.parse(nextDoseDueDateText)?.time ?: 0)
            } catch (e: Exception) {
                null
            }

            dateAdministered?.let { adminDate ->
                nextDoseDueDate?.let { nextDate ->
                    val vaccinationRecord = VaccinationRecord(
                        vaccine_name = vaccineNameText,
                        date_administrated = adminDate,
                        next_dose_due_date = nextDate,
                        manufacturer = manufacturerText,
                        dose = "$dosesTaken/$totalDoses",
                        user_id = null // Set user ID later
                    )

                    // TODO: save to db
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val userId = getId(userEmail)
                            userId?.let {
                                vaccinationRecord.user_id = it
                                VaccinationRecordSF.insertRecord(vaccinationRecord, requireContext())
                                Toast.makeText(requireContext(), "Vaccination record saved!", Toast.LENGTH_SHORT).show()
                            } ?: run {
                                Toast.makeText(requireContext(), "Failed to retrieve user ID", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Failed to save vaccination record", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            } ?: run {
                Toast.makeText(requireContext(), "Failed to parse date", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun getId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            UserSF.getId(email)
        }
    }
}
