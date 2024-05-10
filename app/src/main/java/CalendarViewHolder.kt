import android.widget.FrameLayout
import android.widget.TextView
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import CalendarAdapter.OnItemListener

/**
 * This class is responsible for managing the calendar cell view.
 *
 * @property itemView The view of the calendar cell.
 * @property onItemListener The listener for item click events.
 */
class CalendarViewHolder(itemView: View, private val onItemListener: OnItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    val dayCell: FrameLayout = itemView.findViewById(R.id.day_cell)

    /**
     * Initializes the view holder and sets the click listener.
     */
    init {
        itemView.setOnClickListener(this)
    }

    /**
     * Called when the calendar cell is clicked.
     *
     * @param view The view that was clicked.
     */
    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }
}