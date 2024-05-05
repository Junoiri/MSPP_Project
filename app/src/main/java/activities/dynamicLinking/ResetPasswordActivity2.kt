package activities.dynamicLinking

import activities.LoginActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Authenticator
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties

class ResetPasswordActivity2 : AppCompatActivity() {

    companion object {
    private const val TAG = "ResetPasswordActivity2"
}

    private lateinit var editTextNewPassword: EditText
    private lateinit var buttonSetNewPassword: Button
    private lateinit var imageViewVisibilityToggle: ImageView
    private lateinit var mAuth: FirebaseAuth
    private var email: String? = null
    private lateinit var icons: List<ImageView>

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_reset_password2)

    initializeViews()
    initializeFirebase()
    setEventListeners()

    // Handle the incoming intent
    val data = intent.data
    if (data != null) {
        // The intent contains a password reset link
        val resetToken = data.getQueryParameter("token")
        email = data.getQueryParameter("email")
        if (resetToken != null) {
            // Handle the reset token
            handleResetToken(resetToken)
        }
    }
}
private fun handleResetToken(resetToken: String) {
    // Log the start of the password reset process
    Log.d(TAG, "handleResetToken: started")

    // Verify the reset token
    if (verifyResetToken(resetToken)) {
        // If the token is valid, get the new password from SharedPreferences
        val newPassword = retrievePassword()
        if (newPassword.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "Please enter a new password.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Get the user
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Check if the signed-in user's email matches the email for which the password reset was initiated
            if (user.email == email) {
                // Update the user's password
                user.updatePassword(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Password updated successfully
                        Log.d(TAG, "handleResetToken: password updated")
                        Toast.makeText(
                            this,
                            "Password updated successfully. Please log in with your new password.",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Failed to update password
                        Log.d(TAG, "handleResetToken: failed to update password", task.exception)
                        Toast.makeText(
                            this,
                            "Failed to update password: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                // The signed-in user's email doesn't match
                Log.d(TAG, "handleResetToken: signed-in user's email doesn't match")
                Toast.makeText(
                    this,
                    "Signed-in user's email doesn't match. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // No user is signed in
            Log.d(TAG, "handleResetToken: no user signed in")
            Toast.makeText(
                this,
                "No user signed in. Please sign in and try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    } else {
        // If the token is not valid, show an error message
        Log.d(TAG, "handleResetToken: invalid reset token")
        Toast.makeText(
            this,
            "Invalid reset token. Please try again.",
            Toast.LENGTH_SHORT
        ).show()
    }
}
    private fun retrievePassword(): String? {
    val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
    return sharedPreferences.getString("password", null)
}

    private fun verifyResetToken(resetToken: String): Boolean {
    return try {
        UUID.fromString(resetToken)
        true
    } catch (exception: IllegalArgumentException) {
        false
    }
}
    private fun initializeViews() {
        editTextNewPassword = findViewById(R.id.password_edit_text)
        buttonSetNewPassword = findViewById(R.id.register_button)
        imageViewVisibilityToggle = findViewById(R.id.password_visibility_toggle)

        // Initialize password criteria icons
        icons = listOf(
            findViewById(R.id.condition_1_icon),
            findViewById(R.id.condition_2_icon),
            findViewById(R.id.condition_3_icon),
            findViewById(R.id.condition_4_icon)
        )
    }

    private fun initializeFirebase() {
        mAuth = FirebaseAuth.getInstance()
        email = intent.getStringExtra("email")
    }

    private fun setEventListeners() {
        editTextNewPassword.addTextChangedListener {
            val password = it.toString()
            checkPasswordRequirements(password)
            val isValid = isValidPassword(password)
            buttonSetNewPassword.isEnabled = isValid
            if (isValid) {
                buttonSetNewPassword.setOnClickListener {
                    savePassword(password) // Save the password to SharedPreferences
                    createPasswordResetLink()
                }
            }
        }

    imageViewVisibilityToggle.setOnClickListener {
        if (editTextNewPassword.transformationMethod == null) {
            // Hide password
            editTextNewPassword.transformationMethod =
                android.text.method.PasswordTransformationMethod.getInstance()
            imageViewVisibilityToggle.setImageResource(R.drawable.ic_visibility_off)
        } else {
            // Show password
            editTextNewPassword.transformationMethod = null
            imageViewVisibilityToggle.setImageResource(R.drawable.ic_visibility_on)
        }
    }
}

    private fun savePassword(password: String) {
    val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("password", password)
    editor.apply()
}
    private fun checkPasswordRequirements(password: String) {
        val specialCharacters = "!@#\$%^&*()_+-=<>?/\\|{}[]:;\"'"

        icons[0].setImageResource(
            if (password.length >= 8) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )
        icons[1].setImageResource(
            if (password.any { it.isUpperCase() }) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )
        icons[2].setImageResource(
            if (password.any { it.isDigit() }) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )
        icons[3].setImageResource(
            if (password.any { it in specialCharacters }) R.drawable.ic_check_black else R.drawable.ic_cross_black
        )
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isDigit() } &&
                password.any { "!@#\$%^&*()_+-=<>?/\\|{}[]:;\"'".contains(it) }
    }

    private fun createPasswordResetLink() {
    // Generate a unique token for password reset
    val resetToken = UUID.randomUUID().toString() // Generate unique token
    val deepLinkUri = Uri.Builder()
        .scheme("https")
        .authority("mspp.page.link")
        .path("/resetPassword")
        .appendQueryParameter("token", resetToken)
        .appendQueryParameter("email", email)
        .build()

    // Send the deep link via email
    sendPasswordResetEmail(email!!, deepLinkUri.toString()) // Email sending logic

    Toast.makeText(
        this,
        "A password reset link has been sent to your email.",
        Toast.LENGTH_SHORT
    ).show()
}

    private fun sendPasswordResetEmail(email: String, deepLink: String) {
        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com" // Your SMTP server
        props["mail.smtp.port"] = "587" // SMTP port
        props["mail.smtp.auth"] = "true" // If SMTP server requires authentication
        props["mail.smtp.starttls.enable"] = "true" // Enable STARTTLS

        val auth = object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                return javax.mail.PasswordAuthentication(
                    "polmarpolsad@gmail.com",
                    "yqglfkjdqbsezeif"
                )
            }
        }

        val session = Session.getInstance(props, auth)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val message = MimeMessage(session)
                message.setFrom(InternetAddress("no-reply@example.com"))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
                )
                message.subject = "Password Reset"
                message.setText("Click the link to reset your password: $deepLink")

                Transport.send(message) // Send the email
            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
}

