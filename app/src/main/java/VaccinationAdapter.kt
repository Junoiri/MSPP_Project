import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import db.scheduled_vaccination.ScheduledVaccination
import android.transition.TransitionManager
import android.widget.ImageButton
import com.google.android.material.snackbar.Snackbar

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


            //TODO: Set up the click listener to EditVaccinationRecord or EditUpcomingVaccination based on the date of the vaccination - database
            editButton.setOnClickListener {
                val context = it.context
//                val intent = Intent(context, EditVaccinationActivity::class.java)
                // Pass any extra data to EditVaccineActivity if needed
                // For example, you might want to pass the ID of the vaccination to be edited
                // intent.putExtra("VACCINATION_ID", vaccination.id)
//                context.startActivity(intent)
            }

            deleteButton.setOnClickListener {
                Snackbar.make(
                    it,
                    "Do you want to remove vaccination from the calendar?",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Yes") { _ ->
                        // Perform the deletion here
                        // For example, you might want to call a method in your ViewModel or Repository to delete the vaccination from the database
                        // viewModel.deleteVaccination(vaccination.id)
                    }
                    .show()
            }
        }

        fun bind(vaccination: ScheduledVaccination) {
            // Bind the data to the views here
            // For example:
            // itemView.findViewById<TextView>(R.id.date_administrated).text = vaccination.dateAdministered
            // itemView.findViewById<TextView>(R.id.next_dose_due_date).text = vaccination.nextDoseDueDate
            // etc.
        }
    }
}