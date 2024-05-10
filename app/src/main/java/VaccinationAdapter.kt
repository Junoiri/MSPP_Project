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

/**
 * This class is responsible for managing the vaccination view.
 *
 * @property vaccinations The list of scheduled vaccinations.
 */
class VaccinationAdapter(private val vaccinations: List<ScheduledVaccination>) :
    RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder>() {


    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccination_entry, parent, false)
        return VaccinationViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: VaccinationViewHolder, position: Int) {
        val vaccination = vaccinations[position]
        holder.bind(vaccination)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = vaccinations.size

    /**
     * This class is responsible for managing the vaccination cell view.
     *
     * @property itemView The view of the vaccination cell.
     */
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
