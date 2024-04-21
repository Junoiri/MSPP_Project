package activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import authenticators.EmailAuthManager
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailAuthManager: EmailAuthManager
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var toggleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize FirebaseAuth and EmailAuthManager
        auth = FirebaseAuth.getInstance()
        emailAuthManager = EmailAuthManager(auth, this)

        // Initialize views
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        registerTextView = findViewById(R.id.textView_register)
        toggleButton = findViewById(R.id.button_show_hide_password)

        configurePasswordVisibilityToggle(passwordEditText, toggleButton)

        loginButton.setOnClickListener { loginWithEmail() }

        registerTextView.setOnClickListener { navigateToRegisterActivity() }
    }

    private fun loginWithEmail() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter both email and password.")
            return
        }

        emailAuthManager.signInWithEmailAndPassword(
            email,
            password,
            onSuccess = { user ->
                if (user != null) {
                    showToast("Login successful!")
                    navigateToMainActivity() // Navigate to main activity
                } else {
                    showToast("Login failed. Please try again.")
                }
            },
            onFailure = { errorMsg ->
                showToast(errorMsg)
            }
        )
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Prevents returning to the login activity
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
