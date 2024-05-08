import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import java.time.LocalDate

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var selectedPosition = -1 // Add this line

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonth[position]

        // Get the current day
        val currentDay = LocalDate.now().dayOfMonth.toString()

        if (currentDay == daysOfMonth[position]) {
            holder.dayCell.setBackgroundColor(Color.MAGENTA)
        } else {
            holder.dayCell.setBackgroundColor(Color.TRANSPARENT)
        }

        // Set the background color of the selected cell to light gray
        if (position == selectedPosition) {
            holder.dayCell.setBackgroundColor(Color.LTGRAY)
        }

        // Set an OnClickListener for the day cell
        holder.dayCell.setOnClickListener {
            // Reset the background color of the previously selected cell
            notifyItemChanged(selectedPosition)
            // Update the selected position
            selectedPosition = position
            // Change the background color of the clicked cell to light gray
            it.setBackgroundColor(Color.LTGRAY)
            // Call the onItemClick method of the OnItemListener
            onItemListener.onItemClick(position, daysOfMonth[position])
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}