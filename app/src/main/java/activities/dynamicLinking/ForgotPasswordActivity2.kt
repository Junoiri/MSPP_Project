package activities.dynamicLinking

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

/**
 * This activity is responsible for handling the password recovery process.
 *
 * @property editTextEmail The EditText field for the user's email.
 * @property buttonRecoverPassword The button to initiate the password recovery process.
 */
class ForgotPasswordActivity2 : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var buttonRecoverPassword: Button

    /**
     * Initializes the activity view and sets up the views and event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password2)

        initializeViews()
        setEventListeners()
    }

    /**
     * Initializes the views used in this activity.
     */
    private fun initializeViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonRecoverPassword = findViewById(R.id.buttonRecoverPassword)
    }

    /**
     * Sets up the event listeners for the views in this activity.
     */
    private fun setEventListeners() {
        buttonRecoverPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                editTextEmail.error = "Please enter your email"
                return@setOnClickListener
            }

            // Verify if a user with this email exists
            // userExists(email)

            // Move to the next activity regardless of the user existence
            val intent = Intent(this, ResetPasswordActivity2::class.java).apply {
                putExtra("email", email)
            }
            startActivity(intent)
        }
    }

//private fun userExists(email: String) {
//    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val signInMethods = task.result?.signInMethods
//                if (signInMethods?.isEmpty() == true) {
//                    editTextEmail.error = "No user found with this email"
//                } else {
//                    val intent = Intent(this, ResetPasswordActivity2::class.java).apply {
//                        putExtra("email", email)
//                    }
//                    startActivity(intent)
//                }
//            } else {
//                Toast.makeText(
//                    this,
//                    "Failed to check user: ${task.exception?.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//}
}