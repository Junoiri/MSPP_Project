package activities

import activities.dynamicLinking.ForgotPasswordActivity2
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import authenticators.EmailAuthManager
import authenticators.FacebookAuthManager
import authenticators.GoogleAuthManager
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailAuthManager: EmailAuthManager
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var toggleButton: Button
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var progressBar: ProgressBar


    private lateinit var facebookAuthManager: FacebookAuthManager
    private lateinit var googleAuthManager: GoogleAuthManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Authentication
        auth = FirebaseAuth.getInstance()
        emailAuthManager = EmailAuthManager(auth, this)

        // Initialize views
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        registerTextView = findViewById(R.id.textView_register)
        toggleButton = findViewById(R.id.button_show_hide_password)
        forgotPasswordTextView = findViewById(R.id.textView_forgot_password)
        progressBar = findViewById(R.id.progressBar)

        initializeAuthManagers()
        setupSignInButtons()

        configurePasswordVisibilityToggle(passwordEditText, toggleButton)

        loginButton.setOnClickListener { loginWithEmail() }

        registerTextView.setOnClickListener { navigateToRegisterActivity() }

        forgotPasswordTextView.setOnClickListener { navigateToForgotPasswordActivity() }
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

    private fun navigateToForgotPasswordActivity() {
        //NOTE: Here, we can add '2' at the end to use the dynamic linking approach.
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun configurePasswordVisibilityToggle(
        passwordEditText: EditText,
        toggleButton: Button
    ) {
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

    private fun initializeAuthManagers() {
        val clientId = getString(R.string.client_id)
        facebookAuthManager = FacebookAuthManager(this)
        googleAuthManager = GoogleAuthManager(this, clientId)
    }

    private fun setupSignInButtons() {
        val googleSignInButton: ImageView = findViewById(R.id.google_sign_in_button)
        googleSignInButton.setOnClickListener {
            // Show ProgressBar
            progressBar.visibility = View.VISIBLE

            // Start Google sign-in process
            googleAuthManager.signIn()
        }

        val facebookSignInButton: ImageView = findViewById(R.id.facebook_sign_in_button)
        facebookSignInButton.setOnClickListener {
            // Show ProgressBar
            progressBar.visibility = View.VISIBLE

            // Start Facebook sign-in process
            facebookAuthManager.signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleActivityResult(requestCode, resultCode, data)
    }

    private fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookAuthManager.onActivityResult(requestCode, resultCode, data)
    }
}
