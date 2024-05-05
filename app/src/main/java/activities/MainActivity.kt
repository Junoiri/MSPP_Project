package activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mspp_project.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var drawerMenuHandler: DrawerMenuHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_menu) // Set the menu icon

        drawerMenuHandler = DrawerMenuHandler(this, toolbar, R.string.open, R.string.close)
        setupFabAddNew()

    }

    private fun setupFabAddNew() {
        //FloatingActionButton click listener
        val fabAddNew: FloatingActionButton = findViewById(R.id.fab_add_new)
        fabAddNew.setOnClickListener {
            val intent = Intent(this, AddVaccinationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerMenuHandler.syncState()
    }
}