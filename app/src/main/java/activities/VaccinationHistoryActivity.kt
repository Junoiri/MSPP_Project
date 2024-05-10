package activities

import db.vaccination_record.VaccinationRecord
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
import android.view.animation.Animation

// TODO: load data from the db
class VaccinationHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vaccination_history)
        setupToolbar()
        setupVaccinationRecords()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        val backButton: ImageView = findViewById(R.id.back_button)

        toolbarTitle.text = "History"
        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay, R.anim.pop_out)
        }
    }

    private fun createDraftRecords(): List<VaccinationRecord> {
        // Create some draft records
        return listOf(
            VaccinationRecord(
                record_id = 1,
                vaccine_name = "Vaccine 1",
                date_administrated = java.sql.Date(System.currentTimeMillis()),
                next_dose_due_date = java.sql.Date(System.currentTimeMillis()),
                manufacturer = "Manufacturer 1",
                dose = "Dose 1"
            ),
            VaccinationRecord(
                record_id = 2,
                vaccine_name = "Vaccine 2",
                date_administrated = java.sql.Date(System.currentTimeMillis()),
                next_dose_due_date = java.sql.Date(System.currentTimeMillis()),
                manufacturer = "Manufacturer 2",
                dose = "Dose 2"
            )
        )
    }

    private fun setupVaccinationRecords() {
        val recordsContainer: LinearLayout = findViewById(R.id.entries_container)
        val layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        recordsContainer.layoutTransition = layoutTransition

        val vaccinationRecords = createDraftRecords()

        for (record in vaccinationRecords) {
            val recordView: View = LayoutInflater.from(this)
                .inflate(R.layout.vaccination_entry, recordsContainer, false)

            val vaccineName: TextView = recordView.findViewById(R.id.vaccine_name)
            val dateAdministrated: TextView = recordView.findViewById(R.id.date_administrated)
            val nextDoseDueDate: TextView = recordView.findViewById(R.id.next_dose_due_date)
            val manufacturer: TextView = recordView.findViewById(R.id.manufacturer)
            val dose: TextView = recordView.findViewById(R.id.dose)
            val editButton: ImageButton = recordView.findViewById(R.id.edit_button)

            vaccineName.text = record.vaccine_name
            dateAdministrated.text = record.date_administrated.toString()
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

            recordsContainer.addView(recordView, 0)
        }
    }
}