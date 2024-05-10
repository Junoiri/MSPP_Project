package activities

import VaccinationAdapter
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import db.scheduled_vaccination.ScheduledVaccination
import db.vaccination_record.VaccinationRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * This fragment is responsible for managing the bottom sheet dialog for vaccinations.
 */
class VaccinationBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var dialogTitle: TextView
    private var userEmail: String = "" // Initialize with an empty string

    /**
     * Initializes the fragment view, sets up the dialog title and recycler view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_layout, container, false)
        dialogRecyclerView = view.findViewById(R.id.dialog_recycler_view)
        dialogTitle = view.findViewById(R.id.dialog_title)
        return view
    }

    /**
     * Updates the content of the dialog after the view has been created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateContent()
    }

    /**
     * Creates a dialog that is not cancelable on touch outside.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        return dialog
    }

    /**
     * Updates the content of the dialog, sets the title to the selected date and updates the recycler view with the vaccinations for the selected date.
     */
    fun updateContent() {
        // Retrieve the selected date from the Bundle
        val selectedDateText = requireArguments().getString("selectedDate")

        // Set the text of dialogTitle to the selected date
        dialogTitle.text = selectedDateText

        // Get the vaccinations from the database for the selected day
        CoroutineScope(Dispatchers.Main).launch {
            val user_id = getId(userEmail) ?: return@launch
            val scheduledVaccinations = getScheduledVaccinationsByDate(user_id, parseDate(selectedDateText))
            val vaccinationRecords = getVaccinationRecordsByDate(user_id, parseDate(selectedDateText))

            val vaccinations = scheduledVaccinations?.toMutableList() ?: mutableListOf()
            vaccinationRecords?.forEach { record ->
                vaccinations.add(record)
            }

            
            val adapter = VaccinationAdapter(vaccinations.toList() as List<ScheduledVaccination>)
            dialogRecyclerView.adapter = adapter
        }
    }

    // TODO: Placeholder method to get user ID
    private suspend fun getId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            // Implement your logic here
            null
        }
    }

    // TODO: Placeholder method to get scheduled vaccinations by date
    private suspend fun getScheduledVaccinationsByDate(user_id: Int, date: Date?): Set<ScheduledVaccination?>? {
        return withContext(Dispatchers.IO) {
            // Implement your logic here
            null
        }
    }

    // TODO: Placeholder method to get vaccination records by date
    private suspend fun getVaccinationRecordsByDate(user_id: Int, date: Date?): Set<VaccinationRecord?>? {
        return withContext(Dispatchers.IO) {
            // Implement your logic here
            null
        }
    }

    // TODO: Placeholder method to parse date string to Date object
    private fun parseDate(date: String?): Date? {
        // Implement your logic here
        return null
    }
}

private fun <E> MutableList<E>.add(element: VaccinationRecord?) {

}
