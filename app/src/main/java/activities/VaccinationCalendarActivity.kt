package activities

import CalendarAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import db.scheduled_vaccination.ScheduledVaccination
import db.scheduled_vaccination.ScheduledVaccinationSF
import db.user.UserSF
import db.vaccination_record.VaccinationRecord
import db.vaccination_record.VaccinationRecordSF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * This activity is responsible for managing the vaccination calendar.
 *
 * @property userEmail The email of the current user.
 * @property monthYearText The TextView that displays the current month and year.
 * @property calendarRecyclerView The RecyclerView that displays the calendar.
 * @property selectedDate The currently selected date.
 * @property bottomSheetDialogFragment The BottomSheetDialogFragment that displays the vaccinations for the selected date.
 */
@RequiresApi(Build.VERSION_CODES.O)
class VaccinationCalendarActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private var userEmail = Firebase.auth.currentUser?.email.toString()
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate

    private var bottomSheetDialogFragment: VaccinationBottomSheetDialogFragment? = null

    /**
     * Initializes the activity view, sets up the toolbar, initializes the widgets, and sets the month view.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_calendar)
        setupToolbar()
        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()
    }

    /**
     * Sets up the toolbar with a back button and title.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<LinearLayout>(R.id.toolbar)

        // Find the back button from the included toolbar layout and set its click listener
        val backButton: ImageView = toolbar.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.stay, R.anim.pop_out)

        }

        val titleTextView: TextView = toolbar.findViewById(R.id.toolbar_title)
        titleTextView.text = "Calendar"
    }

    /**
     * Initializes the widgets used in this activity.
     */
    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
    }

    /**
     * Sets the month view in the calendar.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysOfMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysOfMonth, this, selectedDate)
        val layoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }


    /**
     * Returns an ArrayList of days in the month for the given date.
     *
     * @param date The date to get the days of the month for.
     * @return An ArrayList of days in the month for the given date.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    /**
     * Returns a formatted string of the month and year from the given date.
     *
     * @param date The date to get the month and year from.
     * @return A formatted string of the month and year from the given date.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    /**
     * Changes the selected date to the previous month and updates the month view.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction(view: View) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    /**
     * Changes the selected date to the next month and updates the month view.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction(view: View) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    /**
     * Handles the item click event for a day in the calendar.
     *
     * @param position The position of the clicked item in the adapter.
     * @param dayText The text of the clicked day.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, dayText: String?) {
        if (!dayText.isNullOrEmpty()) {
            val selectedDateText = "Selected Date: $dayText ${monthYearFromDate(selectedDate)}"

            // Get the user ID
            GlobalScope.launch(Dispatchers.Main) {
                val userId = getId(userEmail)
                if (userId != null) {
                    // Get the scheduled vaccinations for the selected date
                    val scheduleDate = Date.valueOf(selectedDate.toString())
                    val scheduledVaccinations = ScheduledVaccinationSF.getScheduledVaccinationsByDate(userId, scheduleDate)

                    // Get the vaccination records for the selected date
                    val vaccinationRecords = VaccinationRecordSF.getVaccinationRecordsByDate(userId, scheduleDate)

                    // Process the data as needed
                    handleData(selectedDateText, scheduledVaccinations, vaccinationRecords)
                } else {
                    Toast.makeText(this@VaccinationCalendarActivity, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Returns the ID of the user with the given email.
     *
     * @param email The email of the user.
     * @return The ID of the user.
     */
    private suspend fun getId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            UserSF.getId(email)
        }
    }

    /**
     * Handles the data for the selected date, including scheduled vaccinations and vaccination records.
     *
     * @param selectedDateText The text of the selected date.
     * @param scheduledVaccinations The scheduled vaccinations for the selected date.
     * @param vaccinationRecords The vaccination records for the selected date.
     */
    private fun handleData(
        selectedDateText: String,
        scheduledVaccinations: Set<ScheduledVaccination?>?,
        vaccinationRecords: Set<VaccinationRecord?>?
    ) {
        if (scheduledVaccinations != null && scheduledVaccinations.isNotEmpty()) {
            // If the BottomSheetDialogFragment is not currently displayed, create a new instance
            if (bottomSheetDialogFragment == null) {
                bottomSheetDialogFragment = VaccinationBottomSheetDialogFragment()

                // Create a Bundle to hold the selected date
                val bundle = Bundle()
                bundle.putString("selectedDate", selectedDateText)
                bundle.putSerializable("scheduledVaccinations", scheduledVaccinations.toTypedArray())
                bundle.putSerializable("vaccinationRecords", vaccinationRecords?.toTypedArray())

                // Set the Bundle as an argument to the fragment
                bottomSheetDialogFragment!!.arguments = bundle

                // Show the BottomSheetDialogFragment
                bottomSheetDialogFragment!!.show(
                    supportFragmentManager,
                    "VaccinationBottomSheetDialog"
                )
            } else {
                // If the BottomSheetDialogFragment is currently displayed, update its content
                bottomSheetDialogFragment!!.requireArguments().putString("selectedDate", selectedDateText)
                bottomSheetDialogFragment!!.updateContent()
            }
        } else {
            Toast.makeText(this@VaccinationCalendarActivity, "No vaccinations scheduled for this date.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
