package activities

import CalendarAdapter
import VaccinationAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import db.scheduled_vaccination.ScheduledVaccination
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class VaccinationCalendarActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
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

            // Get the vaccinations from the database for the selected day
            // This is a placeholder and you need to replace it with your actual database call
            val vaccinations = getVaccinationsForDate(selectedDateText)

            if (vaccinations.isNotEmpty()) {
                // If the BottomSheetDialogFragment is not currently displayed, create a new instance
                if (bottomSheetDialogFragment == null) {
                    bottomSheetDialogFragment = VaccinationBottomSheetDialogFragment()

                    // Create a Bundle to hold the selected date
                    val bundle = Bundle()
                    bundle.putString("selectedDate", selectedDateText)

                    // Set the Bundle as an argument to the fragment
                    bottomSheetDialogFragment!!.arguments = bundle

                    // Show the BottomSheetDialogFragment
                    bottomSheetDialogFragment!!.show(
                        supportFragmentManager,
                        "VaccinationBottomSheetDialog"
                    )
                } else {
                    // If the BottomSheetDialogFragment is currently displayed, update its content
                    bottomSheetDialogFragment!!.requireArguments()
                        .putString("selectedDate", selectedDateText)
                    bottomSheetDialogFragment!!.updateContent()
                }
            } else {
                Toast.makeText(this, "No vaccinations scheduled for this date.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // TODO: Placeholder method to get vaccinations from the database
// Replace this with your actual database call
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getVaccinationsForDate(date: String?): List<ScheduledVaccination> {
        // TODO: Implement the database call to get the vaccinations for the given date
        return emptyList()
    }
}