import activities.UpcomingVaccinationFragment
import activities.VaccinationRecordFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            VaccinationRecordFragment()
        } else {
            UpcomingVaccinationFragment()
        }
    }
}