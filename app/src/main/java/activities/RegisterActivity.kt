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
    //Calling firstly insert password suspend function then insert user
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

    private fun setupGoogleSignInButtonListener() {
        val signInButton: ImageView = findViewById(R.id.imageView_option1)
        signInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            googleAuthManager.signIn()
        }
    }

    private fun setupFacebookSignInButtonListener() {
        val signInButton: ImageView = findViewById(R.id.imageView_option2)
        signInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            facebookAuthManager.signIn()
        }
    }

    private fun handleFacebookSignInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookAuthManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupTextWatchers() {
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordRequirements(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

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

    private fun setupLoginListener() {
        val loginTextView = findViewById<TextView>(R.id.textView_login)
        loginTextView.setOnClickListener { navigateToLoginActivity() }
    }

    private fun setupDatePicker() {
        dobEditText.setOnClickListener { showDatePicker() }
    }

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

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left)
    }

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

    private suspend fun insertPassword(): Int {
        val password: String = passwordEditText.text.toString().trim()

        return withContext(Dispatchers.IO) {
            val pass = Password(null, password)
            val passwordId = PasswordSF.insertPassword(pass, this@RegisterActivity)
            passwordId
        }
    }

    private suspend fun insertUser() {
        val name: String = nameEditText.text.toString().trim()
        val surname: String = surnameEditText.text.toString().trim()
        val email: String = emailEditText.text.toString().trim()
        val selectedDateString: String = dobEditText.text.toString().trim()

        val selectedDate: Date? = parseDate(selectedDateString)

        val user = User(null,name, surname, email, selectedDate?.let { toSqlDate(it) }, passId)

        withContext(Dispatchers.IO) {
            UserSF.insertUser(user, this@RegisterActivity)
        }
    }
    private fun parseDate(dateString: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            format.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    private fun toSqlDate(utilDate: Date): java.sql.Date {
        return java.sql.Date(utilDate.time)
    }


    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isDigit() } &&
                password.any { "!@#\$%^&*()_+-=<>?/\\|{}[]:;\"'".contains(it) }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        handleFacebookSignInResult(requestCode, resultCode, data)
    }
}
