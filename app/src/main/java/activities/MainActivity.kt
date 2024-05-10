package activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.mspp_project.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


/**
 * This is the main activity of the application.
 * It provides navigation to other activities such as AddVaccinationActivity, SettingsActivity, UserProfileActivity, VaccinationHistoryActivity, and VaccinationCalendarActivity.
 * It also provides a floating action button for quick access to AddVaccinationActivity.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var menuButton: ImageButton
    private lateinit var slideRight: Animation
    private lateinit var slideLeft: Animation
    private lateinit var expandedToolbar: NavigationView
    private lateinit var collapsedToolbar: RelativeLayout
    private lateinit var fabAddNew: FloatingActionButton
    private lateinit var navSettings: ImageButton

    private lateinit var navUserProfile: Button
    private lateinit var navVaccinationHistory: Button
    private lateinit var navVaccinationCalendar: Button
    private lateinit var closeButton: AppCompatImageView


    /**
     * Initializes the activity view, UI components, and sets up event listeners.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuButton = findViewById(R.id.menu_button)
        expandedToolbar = findViewById(R.id.nav_view)
        collapsedToolbar = findViewById(R.id.custom_toolbar_vertical)
        fabAddNew = findViewById(R.id.fab_add_new)
        navSettings = findViewById(R.id.nav_settings)
        navUserProfile = findViewById(R.id.nav_user_profile)
        navVaccinationHistory = findViewById(R.id.nav_vaccination_history)
        navVaccinationCalendar = findViewById(R.id.nav_vaccination_calendar)
        closeButton = findViewById(R.id.ic_cross_black)


        // Hide the expanded state when the activity starts
        expandedToolbar.visibility = View.GONE

        // Load the animations
        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)

        setupMenuButton()
        setupFabButton()
        setupSettingsButton()
        setupUserProfileButton()
        setupVaccinationHistoryButton()
        setupVaccinationCalendarButton()
        setupCloseButton()

    }

    /**
     * Sets up the menu button to toggle the visibility of the expanded toolbar.
     */
    private fun setupMenuButton() {
        menuButton.setOnClickListener {
            Log.d("MainActivity", "Menu button clicked")
            if (expandedToolbar.visibility == View.VISIBLE) {
                Log.d("MainActivity", "Expanded toolbar is visible, hiding")
                expandedToolbar.startAnimation(slideLeft)
                expandedToolbar.visibility = View.GONE
            } else {
                Log.d("MainActivity", "Expanded toolbar is hidden, showing")
                expandedToolbar.startAnimation(slideRight)
                expandedToolbar.visibility = View.VISIBLE
            }
        }
    }


    /**
     * Sets up the floating action button to navigate to AddVaccinationActivity.
     */
    private fun setupFabButton() {
        fabAddNew.setOnClickListener {
            val intent = Intent(this, AddVaccinationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.pop_in, R.anim.pop_out)
        }
    }

    /**
     * Sets up the settings button to navigate to SettingsActivity.
     */
    private fun setupSettingsButton() {
        navSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.pop_in, R.anim.pop_out)
        }
    }

    /**
     * Sets up the user profile button to navigate to UserProfileActivity.
     */
    private fun setupUserProfileButton() {
        navUserProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.pop_in, R.anim.pop_out)
        }
    }

    /**
     * Sets up the vaccination history button to navigate to VaccinationHistoryActivity.
     */
    private fun setupVaccinationHistoryButton() {
        navVaccinationHistory.setOnClickListener {
            val intent = Intent(this, VaccinationHistoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.pop_in, R.anim.pop_out)
        }
    }

    /**
     * Sets up the vaccination calendar button to navigate to VaccinationCalendarActivity.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupVaccinationCalendarButton() {
        navVaccinationCalendar.setOnClickListener {
            val intent = Intent(this, VaccinationCalendarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.pop_in, R.anim.pop_out)
        }
    }

    /**
     * Sets up the close button to hide the expanded toolbar.
     */
    private fun setupCloseButton() {
        closeButton.setOnClickListener {
            slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
            slideLeft.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                }

                override fun onAnimationEnd(animation: Animation) {
                    expandedToolbar.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {
                }
            })
            expandedToolbar.startAnimation(slideLeft)
        }
    }
}