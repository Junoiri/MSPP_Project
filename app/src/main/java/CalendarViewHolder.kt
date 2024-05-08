import android.widget.FrameLayout
import android.widget.TextView
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mspp_project.R
import CalendarAdapter.OnItemListener

class CalendarViewHolder(itemView: View, private val onItemListener: OnItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    val dayCell: FrameLayout = itemView.findViewById(R.id.day_cell)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }
}