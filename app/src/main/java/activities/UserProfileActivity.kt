package activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth
import db.user.User
import db.user.UserSF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.SnackbarHelper
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

class UserProfileActivity : AppCompatActivity() {

    private var userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var idNumberEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageView
    private var userId: Int = -1
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        initializeViews()

        setupClickListeners()

        setActivityName()

        coroutineScope.launch {
            getId(userEmail)
            updateUser() // Call updateUser function here
        }
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
        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay, R.anim.pop_out)
        }
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

            else -> true
        }
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private suspend fun updateUser() {
        val name: String = nameEditText.text.toString().trim()
        val surname: String = surnameEditText.text.toString().trim()
        val email: String = emailEditText.text.toString().trim()
        val dobString: String = dobEditText.text.toString().trim()

        val dob: Date? = try {
            val utilDate = dateFormat.parse(dobString)
            Date(utilDate?.time ?: 0)
        } catch (e: Exception) {
            null
        }

        coroutineScope.launch {
            try {
                val userId = getId(userEmail) ?: return@launch
                val success = UserSF.updateUser(userId, User(null, name, surname, email, dob, null), this@UserProfileActivity)
                if (success) {
                    showToast("User updated successfully")
                } else {
                    showToast("Failed to update user")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Failed to update user")
            }
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

    private suspend fun getId(email: String): Int? {
        return withContext(Dispatchers.IO) {
            UserSF.getId(email)
        }
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
        overridePendingTransition(R.anim.stay, R.anim.pop_out)
    }
}
