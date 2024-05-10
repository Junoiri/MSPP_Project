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

/**
 * This class is responsible for managing the calendar view.
 *
 * @property daysOfMonth The list of days in the current month.
 * @property onItemListener The listener for item click events.
 * @property selectedDate The currently selected date.
 */
class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
    private val selectedDate: LocalDate
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var selectedPosition = -1
    private var previousSelectedPosition = -1

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666 * 0.5).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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
            holder.dayCell.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.red
                )
            )
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
                it.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.redTransparent
                    )
                )
                // Call the onItemClick method of the OnItemListener
                onItemListener.onItemClick(currentPosition, daysOfMonth[currentPosition])
                // Update the previous selected position
                previousSelectedPosition = selectedPosition
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    /**
     * Interface definition for a callback to be invoked when an item in this adapter has been clicked.
     */
    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}