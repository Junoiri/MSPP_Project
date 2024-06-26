package activities

import ViewPagerAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.mspp_project.R
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar

/**
 * This activity is responsible for adding vaccination records or schedules.
 * It provides two main functionalities: adding a vaccination record and scheduling an upcoming vaccination.
 * The user can switch between these two functionalities using two buttons: `vaccinationRecordButton` and `upcomingVaccinationButton`.
 */
class AddVaccinationActivity : AppCompatActivity() {
    private lateinit var coordinatorLayout: CoordinatorLayout


    /**
     * Initializes the activity view and sets up the toolbar and view pager.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccination)

        coordinatorLayout = findViewById(R.id.coordinator_layout)

        setupToolbar()
        setupViewPagerAndButtons()
    }

    /**
     * Sets up the view pager and the buttons for switching between adding a vaccination record and scheduling an upcoming vaccination.
     */
    private fun setupViewPagerAndButtons() {
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        val vaccinationRecordButton: Button = findViewById(R.id.vaccination_record_button)
        val upcomingVaccinationButton: Button = findViewById(R.id.upcoming_vaccination_button)

        // Set the vaccinationRecordButton to be disabled by default
        vaccinationRecordButton.isEnabled = false

        vaccinationRecordButton.setOnClickListener {
            viewPager.currentItem = 0
            vaccinationRecordButton.isEnabled = false
            upcomingVaccinationButton.isEnabled = true
        }

        upcomingVaccinationButton.setOnClickListener {
            viewPager.currentItem = 1
            upcomingVaccinationButton.isEnabled = false
            vaccinationRecordButton.isEnabled = true
        }

        // Add a page change callback to the ViewPager2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        vaccinationRecordButton.isEnabled = false
                        upcomingVaccinationButton.isEnabled = true
                        coordinatorLayout.setBackgroundResource(R.drawable.background_add_1)
                    }

                    1 -> {
                        upcomingVaccinationButton.isEnabled = false
                        vaccinationRecordButton.isEnabled = true
                        coordinatorLayout.setBackgroundResource(R.drawable.background_add_2)
                    }
                }
            }
        })
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
        titleTextView.text = "Add Vaccination"
    }

    /**
     * Overrides the back button press to show a Snackbar asking the user to confirm their intention to quit.
     * If the user confirms, the activity is finished and a transition animation is played.
     */
    override fun onBackPressed() {
        val snackbar = Snackbar.make(
            findViewById(R.id.coordinator_layout),
            "Do you want to quit? Your progress won't be saved",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Yes") {
            super.onBackPressed()
            finish()
            overridePendingTransition(R.anim.stay, R.anim.pop_out)

        }
        snackbar.show()

        // Dismiss the Snackbar when the user touches outside of it
    }
}