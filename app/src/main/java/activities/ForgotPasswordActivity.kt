package activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

/**
 * This activity is responsible for the password recovery process.
 * It provides functionality to send a password reset email to the user's registered email address.
 */
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var buttonClose: ImageButton
    private lateinit var textViewForgotPasswordHeader: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var buttonRecoverPassword: Button
    private lateinit var mAuth: FirebaseAuth
    private var isResumed = false

    /**
     * Initializes the activity view, UI components, Firebase Authentication and sets up event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        initializeViews() // Initialize UI components
        initializeFirebase() // Initialize Firebase Authentication
        setEventListeners()
    }

    /**
     * Initializes the UI components.
     */
    private fun initializeViews() {
        buttonClose = findViewById(R.id.buttonClose)
        textViewForgotPasswordHeader = findViewById(R.id.textViewForgotPasswordHeader)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonRecoverPassword = findViewById(R.id.buttonRecoverPassword)
    }

    /**
     * Initializes Firebase Authentication.
     */
    private fun initializeFirebase() {
        mAuth = FirebaseAuth.getInstance() // Initialize Firebase
    }

    /**
     * Sets up event listeners for the close button and recover password button.
     */
    private fun setEventListeners() {
        buttonClose.setOnClickListener {
            finish() // Close the activity when clicking the close button
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        buttonRecoverPassword.setOnClickListener {
            recoverPassword() // Trigger password recovery
        }
    }

    /**
     * Triggers the password recovery process by sending a password reset email to the user's registered email address.
     */
    private fun recoverPassword() {
        val email = editTextEmail.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            editTextEmail.error = "Please enter your email"
            return
        }

        mAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener { _ ->
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Reset Password link has been sent to your registered email",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener { e ->
                var errorMessage = "Error sending reset email"
                if (e is FirebaseAuthException) {
                    errorMessage += ": " + e.errorCode
                }
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show() // Notify the user of failure
            }
    }

    /**
     * Sets a flag to true when the activity is paused.
     */
    override fun onPause() {
        super.onPause()
        isResumed = true
    }

    /**
     * Overrides the back button press to navigate to the previous activity with a slide transition.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    /**
     * If the activity is being resumed, navigates to the LoginActivity with a slide transition.
     */
    override fun onResume() {
        super.onResume()
        // If the activity is being resumed, navigate to the LoginActivity
        if (isResumed) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }
    }
}
