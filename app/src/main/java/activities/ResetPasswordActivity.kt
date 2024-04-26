package activities

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.mspp_project.R

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var newPasswordEditText: EditText
    private lateinit var passwordVisibilityToggle: ImageView
    private lateinit var buttonSetNewPassword: Button

    // Icons for password conditions
    private lateinit var conditionMinCharsIcon: ImageView
    private lateinit var conditionUppercaseIcon: ImageView
    private lateinit var conditionNumberIcon: ImageView
    private lateinit var conditionSpecialCharIcon: ImageView

    // Define a TAG for logging
    private val TAG = "ResetPasswordActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        Log.d(TAG, "onCreate()") // Log onCreate event

        initializeViews() // Initialize the UI components
        initializeFirebase() // Initialize Firebase Authentication
        setEventListeners() // Set event listeners
    }

    private fun initializeViews() {
        newPasswordEditText = findViewById(R.id.password_edit_text)
        passwordVisibilityToggle = findViewById(R.id.password_visibility_toggle)
        buttonSetNewPassword = findViewById(R.id.register_button)

        // Initialize icons for password conditions
        conditionMinCharsIcon = findViewById(R.id.condition_1_icon)
        conditionUppercaseIcon = findViewById(R.id.condition_4_icon)
        conditionNumberIcon = findViewById(R.id.condition_2_icon)
        conditionSpecialCharIcon = findViewById(R.id.condition_3_icon)
    }

    private fun initializeFirebase() {
        mAuth = FirebaseAuth.getInstance() // Initialize Firebase
    }

    private fun setEventListeners() {
        buttonSetNewPassword.setOnClickListener {
            setNewPassword() // Trigger password setting
        }

        passwordVisibilityToggle.setOnClickListener {
            togglePasswordVisibility() // Toggle password visibility
        }

        newPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPasswordRequirements(s.toString()) // Validate password conditions
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun togglePasswordVisibility() {
        if (newPasswordEditText.inputType ==
            InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT) {
            newPasswordEditText.inputType =
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility_on)
        } else {
            newPasswordEditText.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility_off)
        }
    }

    private fun setNewPassword() {
        Log.d(TAG, "setNewPassword()") // Log setNewPassword() call

        val newPassword = newPasswordEditText.text.toString().trim()

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            return
        }

        val user = mAuth.currentUser
        user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()

                Log.d(TAG, "Password updated successfully") // Log password update success

                // Navigate to LoginActivity after successful password reset
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent) // Start LoginActivity
                finish() // Close ResetPasswordActivity
            } else {
                Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()

                Log.e(TAG, "Error updating password: ${task.exception}") // Log password update error
            }
        }
    }

    private fun checkPasswordRequirements(password: String) {
        conditionMinCharsIcon.setImageResource(
            if (password.length >= 8) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )

        conditionUppercaseIcon.setImageResource(
            if (password.any { it.isUpperCase() }) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )

        conditionNumberIcon.setImageResource(
            if (password.any { it.isDigit() }) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )

        val specialCharacters = "!@#\$%^&*()_+-=<>?/\\|{}[]:;\"'"
        conditionSpecialCharIcon.setImageResource(
            if (password.any { it in specialCharacters }) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )
    }
}
