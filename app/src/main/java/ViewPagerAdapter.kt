import activities.UpcomingVaccinationFragment
import activities.VaccinationRecordFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * This class is responsible for managing the view pager.
 *
 * @property activity The activity in which this adapter is operating.
 */
class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = 2

    /**
     * Creates the fragment to be displayed at the specified position.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The fragment to be displayed at the specified position.
     */
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            VaccinationRecordFragment()
        } else {
            UpcomingVaccinationFragment()
        }
    }
}