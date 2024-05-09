//package activities
//
//import android.content.Intent
//import android.os.Build
//import android.view.MenuItem
//import android.view.View
//import android.view.animation.AnimationUtils
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import com.google.android.material.navigation.NavigationView
//import com.example.mspp_project.R
//
//@RequiresApi(Build.VERSION_CODES.O)
//class DrawerMenuHandler(
//    private val activity: AppCompatActivity,
//    private val openRes: Int,
//    private val closeRes: Int
//) {
//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var toggle: ActionBarDrawerToggle
//    private lateinit var navigationView: NavigationView
//
//    fun initViews() {
//        drawerLayout = activity.findViewById(R.id.drawer_layout)
//        if (drawerLayout == null) {
//            throw RuntimeException("R.id.drawer_layout not found.")
//        }
//
//        toggle = ActionBarDrawerToggle(activity, drawerLayout, openRes, closeRes)
//
//        navigationView = activity.findViewById(R.id.nav_view)
//        if (navigationView == null) {
//            throw RuntimeException("R.id.nav_view not found.")
//        }
//
//        setupCloseButton()
//        setupSettingsButton()
//        setupUserProfileButton()
//        setupVaccinationHistoryButton()
//        setupVaccinationCalendarButton()
//        syncState()
//    }
//
//    private fun setupDrawerMenu() {
//        syncState()
//    }
//
//    fun openDrawer() {
//        drawerLayout.openDrawer(GravityCompat.START)
//    }
//
//    private fun setupCloseButton() {
//        val closeBtn: View = navigationView.findViewById(R.id.ic_cross_black)
//        closeBtn.setOnClickListener {
//            drawerLayout.closeDrawers()
//        }
//    }
//
//    private fun setupSettingsButton() {
//        val settingsBtn: ImageButton = navigationView.findViewById(R.id.nav_settings)
//        settingsBtn.setOnClickListener {
//            val intent = Intent(activity, SettingsActivity::class.java)
//            activity.startActivity(intent)
//        }
//    }
//
//    private fun setupUserProfileButton() {
//        val userProfileBtn: Button = navigationView.findViewById(R.id.nav_user_profile)
//        userProfileBtn.setOnClickListener {
//            val intent = Intent(activity, UserProfileActivity::class.java)
//            activity.startActivity(intent)
//        }
//    }
//
//    private fun setupVaccinationHistoryButton() {
//        val vaccinationHistoryBtn: Button =
//            navigationView.findViewById(R.id.nav_vaccination_history)
//        vaccinationHistoryBtn.setOnClickListener {
//            val intent = Intent(activity, VaccinationHistoryActivity::class.java)
//            activity.startActivity(intent)
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun setupVaccinationCalendarButton() {
//        val vaccinationCalendarBtn: Button = navigationView.findViewById(R.id.nav_vaccination_calendar)
//        vaccinationCalendarBtn.setOnClickListener {
//            val intent = Intent(activity, VaccinationCalendarActivity::class.java)
//            activity.startActivity(intent)
//        }
//    }
//
//    fun isDrawerOpen(): Boolean {
//        return drawerLayout.isDrawerOpen(GravityCompat.START)
//    }
//
//    fun syncState() {
//        toggle.syncState()
//    }
//}