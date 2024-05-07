package activities

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.example.mspp_project.R

class DrawerMenuHandler(
    private val activity: AppCompatActivity,
    private val toolbar: Toolbar,
    openRes: Int,
    closeRes: Int
) {
    private val drawerLayout: DrawerLayout = activity.findViewById(R.id.drawer_layout)
    private val toggle: ActionBarDrawerToggle =
        ActionBarDrawerToggle(activity, drawerLayout, toolbar, openRes, closeRes)
    private val navigationView: NavigationView = activity.findViewById(R.id.nav_view)


    init {
    setupDrawerMenu()
    setupDrawerListener()
    setupCloseButton()
    setupSettingsButton()
    setupUserProfileButton()
    setupVaccinationHistoryButton()
}

    private fun setupDrawerMenu() {
        syncState()
    }

    private fun setupDrawerListener() {
        drawerLayout.addDrawerListener(toggle)
    }

    private fun setupCloseButton() {
        val closeBtn: View = navigationView.findViewById(R.id.ic_cross_black)
        closeBtn.setOnClickListener {
            drawerLayout.closeDrawers()
        }
    }

    private fun setupSettingsButton() {
        val settingsBtn: ImageButton = navigationView.findViewById(R.id.nav_settings)
        settingsBtn.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private fun setupUserProfileButton() {
        val userProfileBtn: Button = navigationView.findViewById(R.id.nav_user_profile)
        userProfileBtn.setOnClickListener {
            val intent = Intent(activity, UserProfileActivity::class.java)
            activity.startActivity(intent)
        }
    }
    private fun setupVaccinationHistoryButton() {
    val vaccinationHistoryBtn: Button = navigationView.findViewById(R.id.nav_vaccination_history)
    vaccinationHistoryBtn.setOnClickListener {
        val intent = Intent(activity, VaccinationHistoryActivity::class.java)
        activity.startActivity(intent)
    }
}
    fun syncState() {
        toggle.syncState()
    }
}