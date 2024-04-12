package activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText: EditText = findViewById(R.id.email_edit_text)
        val passwordEditText: EditText = findViewById(R.id.password_edit_text)
        val loginButton: Button = findViewById(R.id.login_button)
        val registerTextView: TextView = findViewById(R.id.textView_register)
        val toggleButton: Button = findViewById(R.id.button_show_hide_password)

        configurePasswordVisibilityToggle(passwordEditText, toggleButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            // TODO: Implement login logic here with email and password
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurePasswordVisibilityToggle(passwordEditText: EditText, toggleButton: Button) {
        var isPasswordVisible = false

        toggleButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            passwordEditText.transformationMethod = if (isPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }
    }
}
