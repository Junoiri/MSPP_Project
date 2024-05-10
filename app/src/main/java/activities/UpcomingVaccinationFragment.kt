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
import db.scheduled_vaccination.ScheduledVaccination
import db.scheduled_vaccination.ScheduledVaccinationSF
import db.user.UserSF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * This fragment is responsible for managing the upcoming vaccination schedule.
 */
class UpcomingVaccinationFragment : Fragment() {
    private var userEmail = Firebase.auth.currentUser?.email.toString()
    private lateinit var dateScheduleButton: Button
    private lateinit var saveButton: FloatingActionButton
    private lateinit var vaccineName: EditText
    private lateinit var manufacturer: EditText
    private lateinit var totalDosesNumberPicker: NumberPicker
    private lateinit var dosesTakenNumberPicker: NumberPicker
    private var selectedDate: Date? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_schedule_layout, container, false)
    }

    /**
     * Initializes the fragment view, sets up the date picker and save button.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupDatePicker()
        setupSaveButton()
    }

    private fun initializeViews(view: View) {
        dateScheduleButton = view.findViewById(R.id.date_schedule)
        saveButton = view.findViewById(R.id.save_vaccination)
        vaccineName = view.findViewById(R.id.vaccine_name)
        manufacturer = view.findViewById(R.id.manufacturer)
        totalDosesNumberPicker = view.findViewById(R.id.dose_total_number_picker)
        dosesTakenNumberPicker = view.findViewById(R.id.dose_taken_number_picker)
    }

    /**
     * Sets up the date picker dialog for scheduling the vaccination.
     */
    private fun setupDatePicker() {
        dateScheduleButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // Display the selected date on the button
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = calendar.time
                    dateScheduleButton.text = "Date schedule: ${sdf.format(selectedDate)}"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    /**
     * Sets up the save button to save the vaccination schedule.
     */
    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val vaccineNameText = vaccineName.text.toString()
            val manufacturerText = manufacturer.text.toString()
            val scheduleDateText = dateScheduleButton.text.toString()

            if (vaccineNameText.isEmpty()) {
                Toast.makeText(requireContext(), "Vaccine name cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (manufacturerText.isEmpty()) {
                Toast.makeText(requireContext(), "Manufacturer cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (scheduleDateText == "Date schedule") {
                Toast.makeText(
                    requireContext(),
                    "Please select a schedule date",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (totalDosesNumberPicker.value == 0 || dosesTakenNumberPicker.value == 0) {
                Toast.makeText(
                    requireContext(),
                    "Please select the number of doses",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val totalDoses = totalDosesNumberPicker.value
            val dosesTaken = dosesTakenNumberPicker.value

            selectedDate?.let { date ->
                val scheduledVaccination = ScheduledVaccination(
                    vaccine_name = vaccineNameText,
                    schedule_date = java.sql.Date(selectedDate?.time ?: 0),
                    manufacturer = manufacturerText,
                    dose = "$dosesTaken/$totalDoses",
                    user_id = null // Set user ID later
                )

                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val userId = getId(userEmail)
                        userId?.let {
                            scheduledVaccination.user_id = it
                            ScheduledVaccinationSF.insertSchedule(
                                scheduledVaccination,
                                requireContext()
                            )
                            Toast.makeText(
                                requireContext(),
                                "Vaccination schedule saved!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } ?: run {
                            Toast.makeText(
                                requireContext(),
                                "Failed to retrieve user ID",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to save vaccination schedule",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }
                }
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "Please select a schedule date",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Retrieves the user ID for the given email.
     * @param email The email to retrieve the user ID for.
     * @return The user ID, or null if not found.
     */
    private suspend fun getId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            UserSF.getId(email)
        }
    }
}
