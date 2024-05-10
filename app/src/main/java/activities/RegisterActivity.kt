package activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import authenticators.FacebookAuthManager
import authenticators.GoogleAuthManager
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import db.password.Password
import db.password.PasswordSF
import db.user.User
import db.user.UserSF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

/**
 * This activity is responsible for user registration.
 * It provides functionalities: register with email and password, register with Google, register with Facebook, and navigation to login activity.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var idNumberEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var passwordVisibilityToggle: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var icons: List<ImageView>
    private lateinit var progressBar: ProgressBar
    private var passId: Int = -1

    private lateinit var googleAuthManager: GoogleAuthManager
    private lateinit var facebookAuthManager: FacebookAuthManager

    /**
     * Initializes the activity view, UI components, Firebase Authentication and sets up event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        googleAuthManager = GoogleAuthManager(this, getString(R.string.client_id))
        facebookAuthManager = FacebookAuthManager(this)

        // Initialize Firebase and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize UI elements and set up listeners
        initializeViews()
        configurePasswordVisibilityToggle()
        setRegisterButtonListener()
        setupGoogleSignInButtonListener()
        setupFacebookSignInButtonListener()
        setupTextWatchers()
        setupLoginListener()
        setupDatePicker()
    }

    private fun initializeViews() {
        emailEditText = findViewById(R.id.email_edit_text)
        nameEditText = findViewById(R.id.name_edit_text)
        surnameEditText = findViewById(R.id.surname_edit_text)
        dobEditText = findViewById(R.id.dob_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        registerButton = findViewById(R.id.register_button)
        passwordVisibilityToggle = findViewById(R.id.password_visibility_toggle)
        progressBar = findViewById(R.id.progressBar)

        // Initialize password criteria icons
        icons = listOf(
            findViewById<ImageView>(R.id.condition_1_icon),
            findViewById<ImageView>(R.id.condition_2_icon),
            findViewById<ImageView>(R.id.condition_3_icon),
            findViewById<ImageView>(R.id.condition_4_icon)
        )
    }

    /**
     * Configures the visibility toggle for the password field.
     */
    private fun configurePasswordVisibilityToggle() {
        passwordVisibilityToggle.setOnClickListener { togglePasswordVisibility() }
    }

    private fun togglePasswordVisibility() {
        if (passwordEditText.inputType == 129) {
            passwordEditText.inputType = 144
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility_on)
        } else {
            passwordEditText.inputType = 129
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility_off)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun setRegisterButtonListener() {
        registerButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val passwordId = insertPassword()
                if (passwordId != -1) {
                    passId = passwordId
                    insertUser()
                } else {
                    showToast("Failed to insert password")
                }
            }
        }
    }

    /**
     * Sets up the Google sign-in button listener.
     * When the button is clicked, it makes the progress bar visible and triggers the Google sign-in process.
     */
    private fun setupGoogleSignInButtonListener() {
        val signInButton: ImageView = findViewById(R.id.imageView_option1)
        signInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            googleAuthManager.signIn()
        }
    }

    /**
     * Sets up the Facebook sign-in button listener.
     * When the button is clicked, it makes the progress bar visible and triggers the Facebook sign-in process.
     */
    private fun setupFacebookSignInButtonListener() {
        val signInButton: ImageView = findViewById(R.id.imageView_option2)
        signInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            facebookAuthManager.signIn()
        }
    }

    /**
     * Handles the result from the Facebook sign-in process.
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    private fun handleFacebookSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookAuthManager.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Sets up text watchers for the password edit text.
     * It checks the password requirements every time the text in the password edit text changes.
     */
    private fun setupTextWatchers() {
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordRequirements(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Checks if the given password meets the requirements.
     * The requirements are: at least 8 characters long, contains an uppercase letter, contains a digit, and contains a special character.
     * @param password The password to check.
     */
    private fun checkPasswordRequirements(password: String) {
        val specialCharacters = "!@#\$%^&*()_+-=<>?/\\|{}[]:;\"'"

        icons[0].setImageResource(
            if (password.length >= 8) R.drawable.ic_check_g else R.drawable.ic_close
        )
        icons[1].setImageResource(
            if (password.any { it.isUpperCase() }) R.drawable.ic_check_g else R.drawable.ic_close
        )
        icons[2].setImageResource(
            if (password.any { it.isDigit() }) R.drawable.ic_check_g else R.drawable.ic_close
        )
        icons[3].setImageResource(
            if (password.any { it in specialCharacters }) R.drawable.ic_check_g else R.drawable.ic_close
        )
    }

    /**
     * Sets up the login text view listener.
     * When the text view is clicked, it navigates to the login activity.
     */
    private fun setupLoginListener() {
        val loginTextView = findViewById<TextView>(R.id.textView_login)
        loginTextView.setOnClickListener { navigateToLoginActivity() }
    }

    /**
     * Sets up the date picker for the date of birth edit text.
     * When the edit text is clicked, it shows the date picker dialog.
     */
    private fun setupDatePicker() {
        dobEditText.setOnClickListener { showDatePicker() }
    }

    /**
     * Shows the date picker dialog.
     * The user can select a date, and the selected date will be set to the date of birth edit text.
     */
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
                dobEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    /**
     * Triggers the registration process with email and password.
     */
    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()
        val dob = dobEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (validateInput(email, name, surname, dob, password)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val userData = hashMapOf(
                            "userId" to userId,
                            "email" to email,
                            "name" to name,
                            "surname" to surname,
                            "dob" to dob,
                        )
                        saveUserToFirestore(userData)
                    } else {
                        showToast("Registration failed: ${task.exception?.localizedMessage}")
                    }
                }
        }
    }

    /**
     * Saves user to Firestore.
     */
    private fun saveUserToFirestore(userData: HashMap<String, String>) {
        db.collection("Users").document(userData["userId"].toString())
            .set(userData)
            .addOnSuccessListener {
                showToast("User registered successfully")
                navigateToLoginActivity()
            }
            .addOnFailureListener { e ->
                showToast("Error saving user: ${e.localizedMessage}")
            }
    }

    /**
     * Navigates to the LoginActivity.
     */
    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left)
    }

    /**
     * Performs input validation for given fields
     */
    private fun validateInput(
        email: String,
        name: String,
        surname: String,
        dob: String,
        password: String
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

            !isValidPassword(password) -> {
                showToast("Password must be at least 8 characters, contain a number, a special character, and an uppercase letter")
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

    /**
     * Inserts the password into the database.
     * @return The ID of the inserted password.
     */
    private suspend fun insertPassword(): Int {
        val password: String = passwordEditText.text.toString().trim()

        return withContext(Dispatchers.IO) {
            val pass = Password(null, password)
            val passwordId = PasswordSF.insertPassword(pass, this@RegisterActivity)
            passwordId
        }
    }

    /**
     * Inserts the user into the database.
     */
    private suspend fun insertUser() {
        val name: String = nameEditText.text.toString().trim()
        val surname: String = surnameEditText.text.toString().trim()
        val email: String = emailEditText.text.toString().trim()
        val selectedDateString: String = dobEditText.text.toString().trim()

        val selectedDate: Date? = parseDate(selectedDateString)

        val user = User(null, name, surname, email, selectedDate?.let { toSqlDate(it) }, passId)

        withContext(Dispatchers.IO) {
            UserSF.insertUser(user, this@RegisterActivity)
        }
    }

    /**
     * Parses the given date string into a Date object.
     * @param dateString The date string to parse.
     * @return The parsed Date object, or null if the date string is invalid.
     */
    private fun parseDate(dateString: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            format.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Converts the given util Date to a SQL Date.
     * @param utilDate The util Date to convert.
     * @return The converted SQL Date.
     */
    private fun toSqlDate(utilDate: Date): java.sql.Date {
        return java.sql.Date(utilDate.time)
    }


    /**
     * Checks if the given password is valid.
     * A valid password is at least 8 characters long, contains an uppercase letter, contains a digit, and contains a special character.
     * @param password The password to check.
     * @return True if the password is valid, false otherwise.
     */
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isDigit() } &&
                password.any { "!@#\$%^&*()_+-=<>?/\\|{}[]:;\"'".contains(it) }
    }

    /**
     * Displays a toast message.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Overrides the back button press to navigate to the previous activity with a slide transition.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    /**
     * Handles the result from the authentication process.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        handleFacebookSignInResult(requestCode, resultCode, data)
    }
}
