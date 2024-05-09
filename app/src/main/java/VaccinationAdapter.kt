import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import db.scheduled_vaccination.ScheduledVaccination
import android.transition.TransitionManager
import android.widget.ImageButton
import com.google.android.material.snackbar.Snackbar

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