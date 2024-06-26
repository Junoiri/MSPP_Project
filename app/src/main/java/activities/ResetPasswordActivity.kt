package activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import db.password.PasswordSF
import db.user.UserSF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This activity is responsible for resetting the user's password.
 */
class ResetPasswordActivity : AppCompatActivity() {

    private var userEmail = Firebase.auth.currentUser?.email.toString()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var newPasswordEditText: EditText
    private lateinit var passwordVisibilityToggle: ImageView
    private lateinit var buttonSetNewPassword: Button
    private val TAG = "ResetPasswordActivity"

    // Icons for password conditions
    private lateinit var conditionMinCharsIcon: ImageView
    private lateinit var conditionUppercaseIcon: ImageView
    private lateinit var conditionNumberIcon: ImageView
    private lateinit var conditionSpecialCharIcon: ImageView

    /**
     * Initializes the activity view, Firebase Authentication, and sets up event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initializeViews()
        initializeFirebase()
        setEventListeners()
    }

    private fun initializeViews() {
        newPasswordEditText = findViewById(R.id.password_edit_text)
        passwordVisibilityToggle = findViewById(R.id.password_visibility_toggle)
        buttonSetNewPassword = findViewById(R.id.register_button)
    }

    /**
     * Initializes Firebase Authentication.
     */
    private fun initializeFirebase() {
        mAuth = FirebaseAuth.getInstance()
    }

    private fun setEventListeners() {
        buttonSetNewPassword.setOnClickListener {
            setNewPassword()
        }

        passwordVisibilityToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        newPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPasswordRequirements(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Toggles the visibility of the password in the password edit text.
     */
    private fun togglePasswordVisibility() {
        if (newPasswordEditText.inputType ==
            InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        ) {
            newPasswordEditText.inputType =
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility_on)
        } else {
            newPasswordEditText.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility_off)
        }
    }

    /**
     * Sets the new password for the user.
     */
    private fun setNewPassword() {
        val newPassword = newPasswordEditText.text.toString().trim()

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val user = mAuth.currentUser
            val userPId = getPId(userEmail)
            if (user != null && userPId != null) {
                val result = updatePassword(userPId, newPassword, applicationContext)
                if (result) {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Password updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Error updating password",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "Error updating password")
                }
            } else {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Failed to retrieve user information",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "Failed to retrieve user information")
            }
        }
    }

    /**
     * Retrieves the password ID for the given email.
     * @param email The email to retrieve the password ID for.
     * @return The password ID, or null if not found.
     */
    private suspend fun getPId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            UserSF.getPasswordId(email)
        }
    }

    /**
     * Updates the password in the database.
     * @param password_id The ID of the password to update.
     * @param newPassword The new password.
     * @param context The context to use.
     * @return True if the password was updated successfully, false otherwise.
     */
    private suspend fun updatePassword(
        password_id: Int,
        newPassword: String,
        context: Context
    ): Boolean {
        return withContext(Dispatchers.IO) {
            PasswordSF.updatePassword(password_id, newPassword, context)
        }
    }

    /**
     * Checks if the given password meets the requirements.
     * The requirements are: at least 8 characters long, contains an uppercase letter, contains a digit, and contains a special character.
     * @param password The password to check.
     */
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


    /**
     * Overrides the onResume method to navigate to the LoginActivity.
     */
    override fun onResume() {
        super.onResume()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
