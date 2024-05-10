package activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mspp_project.R
import android.content.SharedPreferences

/**
 * This activity is responsible for managing the settings of the application.
 */
class SettingsActivity : AppCompatActivity() {
    private lateinit var notificationSwitch: SwitchCompat
    private lateinit var switch2: SwitchCompat
    private lateinit var switch3: SwitchCompat

    /**
     * Initializes the activity view, sets up the toolbar, switches, and window insets.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupToolbar()
        setupSwitches()
        enableEdgeToEdge()
        setupWindowInsets()
    }

    /**
     * Sets up the toolbar with a back button and title.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<LinearLayout>(R.id.toolbar)

        // Find the back button from the included toolbar layout and set its click listener
        val backButton: ImageView = toolbar.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // Finish the SettingsActivity and go back
            overridePendingTransition(R.anim.stay, R.anim.pop_out)
        }

        val titleTextView: TextView = toolbar.findViewById(R.id.toolbar_title)
        titleTextView.text = "Settings"
    }

    /**
     * Sets up the switches for various settings.
     * It loads the saved state of the switches and sets up listeners to save their state when changed.
     */
    private fun setupSwitches() {
        // Find the switches
        notificationSwitch = findViewById(R.id.notification_switch)
        switch2 = findViewById(R.id.setting_switch_2)
        switch3 = findViewById(R.id.setting_switch_3)

        // Load saved state
        val sharedPref = getSharedPreferences("Settings", MODE_PRIVATE)
        notificationSwitch.isChecked = sharedPref.getBoolean("notifications_enabled", true)
        switch2.isChecked = sharedPref.getBoolean("switch2_state", false)
        switch3.isChecked = sharedPref.getBoolean("switch3_state", false)

        // Set up listeners to save state
        setupNotificationSwitch(sharedPref)
        setupSwitch2(sharedPref)
        setupSwitch3(sharedPref)
    }

    /**
     * Sets up the notification switch.
     * It saves its state when changed and shows a toast message indicating whether notifications are enabled or disabled.
     * @param sharedPref The shared preferences to save the state of the switch.
     */
    private fun setupNotificationSwitch(sharedPref: SharedPreferences) {
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPref.edit()) {
                putBoolean("notifications_enabled", isChecked)
                apply()
            }
            if (isChecked) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sets up the second switch.
     * It saves its state when changed.
     * @param sharedPref The shared preferences to save the state of the switch.
     */
    private fun setupSwitch2(sharedPref: SharedPreferences) {
        switch2.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPref.edit()) {
                putBoolean("switch2_state", isChecked)
                apply()
            }
        }
    }


    private fun setupSwitch3(sharedPref: SharedPreferences) {
        switch3.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPref.edit()) {
                putBoolean("switch3_state", isChecked)
                apply()
            }
        }
    }

    /**
     * Sets up the window insets for the activity.
     */
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}