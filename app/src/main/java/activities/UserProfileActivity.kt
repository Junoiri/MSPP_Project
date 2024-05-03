package activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mspp_project.R
import utils.SnackbarHelper
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import android.widget.LinearLayout
import android.widget.TextView
import android.util.Log

//TODO: Implement the hints for input fields, block the email field from being edited

class UserProfileActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var idNumberEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        initializeViews()

        setupClickListeners()

        setActivityName()
    }

    private fun initializeViews() {

        nameEditText = findViewById(R.id.name_edit_text)
        surnameEditText = findViewById(R.id.surname_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        dobEditText = findViewById(R.id.dob_edit_text)
        idNumberEditText = findViewById(R.id.id_number_edit_text)
        saveButton = findViewById(R.id.save_button)

        val toolbarLayout = findViewById<LinearLayout>(R.id.toolbar)
        backButton = toolbarLayout.findViewById(R.id.back_button)
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener { finish() }
        saveButton.setOnClickListener { saveChanges() }
    }

    private fun setActivityName() {
    val toolbarLayout = findViewById<LinearLayout>(R.id.toolbar)
    val titleTextView: TextView = toolbarLayout.findViewById(R.id.toolbar_title)
    titleTextView.text = "User Profile"
}

    private fun saveChanges() {
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val email = emailEditText.text.toString()
        val dob = dobEditText.text.toString()
        val idNumber = idNumberEditText.text.toString()

        // Validate input
        if (validateInput(email, name, surname, dob, idNumber)) {
            // Mock saving changes
            Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(
        email: String,
        name: String,
        surname: String,
        dob: String,
        idNumber: String
    ): Boolean {
        return when {
            !isValidEmail(email) -> {
                showToast("Invalid email address")
                false
            }

            name.isBlank() -> {
                showToast("Name cannot be empty")
                false
            }

            surname.isBlank() -> {
                showToast("Surname cannot be empty")
                false
            }

            !isValidDate(dob) -> {
                showToast("Invalid date of birth")
                false
            }

            !isValidIdNumber(idNumber) -> {
                showToast("Invalid ID Number")
                false
            }

            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
        return pattern.matcher(email).matches()
    }

    private fun isValidDate(date: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.isLenient = false
        return try {
            sdf.parse(date) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidIdNumber(idNumber: String): Boolean {
        return idNumber.matches("\\d{11}".toRegex())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        SnackbarHelper.showSaveChangesSnackbar(
            this,
            yesAction = { saveChanges() },
            noAction = { super.onBackPressed() }
        )
    }
}