package activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import com.bumptech.glide.Glide

/**
 * This activity is responsible for displaying the splash screen of the application.
 */
class SplashActivity : AppCompatActivity() {

    /**
     * Initializes the activity view and sets up the splash screen animation.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val gifView = findViewById<ImageView>(R.id.gifView)
        Glide.with(this).load(R.drawable.anim_circle).into(gifView)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.slide_out_up_splash)
        }, 3000)
    }
}