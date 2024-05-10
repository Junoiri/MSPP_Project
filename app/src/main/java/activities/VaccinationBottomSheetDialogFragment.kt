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

class VaccinationBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var dialogTitle: TextView

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateContent()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        return dialog
    }

    // Add a method to update the content of the BottomSheetDialogFragment
    fun updateContent() {
        // Retrieve the selected date from the Bundle
        val selectedDateText = requireArguments().getString("selectedDate")

        // Set the text of dialogTitle to the selected date
        dialogTitle.text = selectedDateText

        // Get the vaccinations from the database for the selected day
        // This is a placeholder and you need to replace it with your actual database call
        val vaccinations = getVaccinationsForDate(selectedDateText)

        val adapter = VaccinationAdapter(vaccinations)
        dialogRecyclerView.adapter = adapter
    }

    // TODO: Placeholder method to get vaccinations from the database
    // Replace this with your actual database call
    private fun getVaccinationsForDate(date: String?): List<ScheduledVaccination> {
        // TODO: Implement the database call to get the vaccinations for the given date
        //procedure, queries, sf, implementation
        return emptyList()
    }
}