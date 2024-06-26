package activities

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mspp_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import db.user.UserSF
import db.vaccination_record.VaccinationRecord
import db.vaccination_record.VaccinationRecordSF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This activity is responsible for managing the vaccination history.
 */
class VaccinationHistoryActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var userEmail: String

    /**
     * Initializes the activity view, sets up the toolbar, and loads the vaccination records.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vaccination_history)
        setupToolbar()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userEmail = Firebase.auth.currentUser?.email.toString()

        coroutineScope.launch {
            val userId = getId(userEmail)
            userId?.let { loadVaccinationRecords(it) }
        }
    }

    /**
     * Sets up the toolbar with a back button and title.
     */
    private fun setupToolbar() {
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        val backButton: ImageView = findViewById(R.id.back_button)

        toolbarTitle.text = "History"
        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay, R.anim.pop_out)
        }
    }

    /**
     * Loads the vaccination records for the current user.
     */
    private suspend fun loadVaccinationRecords(userId: Int) {
        val recordsContainer: LinearLayout = findViewById(R.id.entries_container)
        recordsContainer.removeAllViews() // Clear previous records

        val layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        recordsContainer.layoutTransition = layoutTransition

        val vaccinationRecords = getAllRecords(userId)
        vaccinationRecords?.let {
            for (record in it) {
                record?.let { // Handle nullable record
                    addRecordToContainer(it, recordsContainer)
                }
            }
        }
    }

    /**
     * Adds a vaccination record to the container view.
     */
    private suspend fun addRecordToContainer(record: VaccinationRecord, container: LinearLayout) {
        val recordView: View = LayoutInflater.from(this)
            .inflate(R.layout.vaccination_entry, container, false)

        val vaccineName: TextView = recordView.findViewById(R.id.vaccine_name)
        val dateAdministered: TextView = recordView.findViewById(R.id.date_administrated)
        val nextDoseDueDate: TextView = recordView.findViewById(R.id.next_dose_due_date)
        val manufacturer: TextView = recordView.findViewById(R.id.manufacturer)
        val dose: TextView = recordView.findViewById(R.id.dose)
        val editButton: ImageButton = recordView.findViewById(R.id.edit_button)

        vaccineName.text = record.vaccine_name
        dateAdministered.text = record.date_administrated.toString()
        nextDoseDueDate.text = record.next_dose_due_date.toString()
        manufacturer.text = record.manufacturer
        dose.text = record.dose

        recordView.setOnClickListener {
            val expandedState: View = recordView.findViewById(R.id.expandable_part)
            if (expandedState.visibility == View.GONE) {
                // Expand the entry
                expandedState.visibility = View.VISIBLE
            } else {
                // Collapse the entry
                expandedState.visibility = View.GONE
            }
        }

        editButton.setOnClickListener {
            // Handle the edit action here
        }

        container.addView(recordView, 0)
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

    /**
     * Retrieves all vaccination records for the given user ID.
     * @param userId The user ID to retrieve the vaccination records for.
     * @return A set of vaccination records for the given user ID.
     */
    private suspend fun getAllRecords(userId: Int): Set<VaccinationRecord?>? {
        return VaccinationRecordSF.getAllRecords(userId)
    }
}
