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

@RequiresApi(Build.VERSION_CODES.O)
class VaccinationCalendarActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private var userEmail = Firebase.auth.currentUser?.email.toString()
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate

    private var bottomSheetDialogFragment: VaccinationBottomSheetDialogFragment? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_calendar)
        setupToolbar()
        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()
    }

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

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysOfMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysOfMonth, this, selectedDate)
        val layoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction(view: View) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction(view: View) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

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

    private suspend fun getId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            UserSF.getId(email)
        }
    }

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
