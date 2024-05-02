package activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mspp_project.R

class MainActivity : AppCompatActivity() {

    //TODO: Implement custom snackbar that will appear when not all information is filled in the User Profile
    // activity - check the data with database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}