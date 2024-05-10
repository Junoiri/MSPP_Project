import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import com.google.android.material.snackbar.Snackbar
import db.scheduled_vaccination.ScheduledVaccination
import db.scheduled_vaccination.ScheduledVaccinationSF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class VaccinationAdapter(private val vaccinations: List<ScheduledVaccination>) :
    RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccination_entry, parent, false)
        return VaccinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccinationViewHolder, position: Int) {
        val vaccination = vaccinations[position]
        holder.bind(vaccination)
    }

    override fun getItemCount() = vaccinations.size

    inner class VaccinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val expandableView: View = itemView.findViewById(R.id.expandable_part)
        private val editButton: ImageButton = itemView.findViewById(R.id.edit_button)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)

        init {
            itemView.setOnClickListener {
                TransitionManager.beginDelayedTransition(itemView as ViewGroup)
                expandableView.visibility = if (expandableView.visibility == View.GONE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            editButton.setOnClickListener {
                // Handle editing of vaccination record
                val vaccination = vaccinations[adapterPosition]
                val context = itemView.context
                vaccination.schedule_date?.let { scheduleDate ->
                    if (scheduleDate.time > getCurrentDate().time) {
                        // If the vaccination date is in the future, navigate to EditUpcomingVaccination
                        navigateToEditUpcomingVaccination(context, vaccination)
                    } else {
                        // If the vaccination date is in the past, navigate to EditVaccinationRecord
                        navigateToEditVaccinationRecord(context, vaccination)
                    }
                }
            }

            deleteButton.setOnClickListener {
                // Handle deletion of vaccination record
                val vaccination = vaccinations[adapterPosition]
                val context = itemView.context
                showDeleteConfirmationSnackbar(context, vaccination)
            }
        }

        fun bind(vaccination: ScheduledVaccination) {
            // Bind the data to the views here
            // For example:
            // itemView.findViewById<TextView>(R.id.date_administrated).text = vaccination.dateAdministered
            // itemView.findViewById<TextView>(R.id.next_dose_due_date).text = vaccination.nextDoseDueDate
            // etc.
        }

        private fun getCurrentDate(): Date {
            return Date()
        }

        private fun navigateToEditUpcomingVaccination(context: Context, vaccination: ScheduledVaccination) {
            // Navigate to EditUpcomingVaccinationActivity
            // val intent = Intent(context, EditUpcomingVaccinationActivity::class.java)
            // Add any necessary extras to the intent
            // context.startActivity(intent)
        }

        private fun navigateToEditVaccinationRecord(context: Context, vaccination: ScheduledVaccination) {
            // Navigate to EditVaccinationRecordActivity
            // val intent = Intent(context, EditVaccinationRecordActivity::class.java)
            // Add any necessary extras to the intent
            // context.startActivity(intent)
        }

        private fun showDeleteConfirmationSnackbar(context: Context, vaccination: ScheduledVaccination) {
            Snackbar.make(
                itemView,
                "Do you want to remove vaccination from the calendar?",
                Snackbar.LENGTH_LONG
            )
                .setAction("Yes") { _ ->
                    // Perform the deletion here
                    deleteVaccination(context, vaccination)
                }
                .show()
        }

        private fun deleteVaccination(context: Context, vaccination: ScheduledVaccination) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val result = ScheduledVaccinationSF.deleteSchedule(vaccination.schedule_id ?: -1)
                    if (result) {
                        // Deletion successful
                        Toast.makeText(context, "Schedule deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        // Deletion failed
                        Toast.makeText(context, "Schedule deletion failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Handle any exceptions here
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
