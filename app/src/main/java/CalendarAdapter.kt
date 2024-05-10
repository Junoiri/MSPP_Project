import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import java.time.LocalDate

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
    private val selectedDate: LocalDate
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var selectedPosition = -1
    private var previousSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666 * 0.5).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
    holder.dayOfMonth.text = daysOfMonth[position]

    // Get the current date
    val currentDate = LocalDate.now()

    // Get the date of the current cell
    val cellDate = if (daysOfMonth[position].isNotEmpty()) {
        selectedDate.withDayOfMonth(daysOfMonth[position].toInt())
    } else {
        null
    }

    // If the cell date is the same as the current date, change the background color
    if (currentDate == cellDate) {
        holder.dayCell.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
    } else {
        holder.dayCell.setBackgroundColor(Color.TRANSPARENT)
    }

    // Set an OnClickListener for the day cell
    holder.dayCell.setOnClickListener {
        val currentPosition = holder.adapterPosition
        // Only change the background color and update the selected position if the cell contains a date
        if (daysOfMonth[currentPosition].isNotEmpty()) {
            // Reset the background color of the previously selected cell
            notifyItemChanged(selectedPosition)
            // Update the selected position
            selectedPosition = currentPosition
            // Change the background color of the clicked cell to light gray
            it.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.redTransparent))
            // Call the onItemClick method of the OnItemListener
            onItemListener.onItemClick(currentPosition, daysOfMonth[currentPosition])
            // Update the previous selected position
            previousSelectedPosition = selectedPosition
        }
    }
}

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}