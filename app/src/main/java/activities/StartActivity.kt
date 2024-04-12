package activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mspp_project.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.register_button)
        registerButton.setOnClickListener {
            val intent = Intent(this@StartActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}