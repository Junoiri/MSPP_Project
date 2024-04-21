package activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

// TODO: Change the data storing method from Firestore to php

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var idNumberEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize UI elements
        initializeViews()

        // Initialize Register button
        registerButton = findViewById(R.id.register_button)

        // Password criteria icons
        val icons = listOf(
            findViewById<ImageView>(R.id.password_criteria_1_icon),
            findViewById<ImageView>(R.id.password_criteria_2_icon),
            findViewById<ImageView>(R.id.password_criteria_3_icon),
            findViewById<ImageView>(R.id.password_criteria_4_icon)
        )


        // Password visibility toggle
        configurePasswordVisibilityToggle()

        // TextWatcher to check password criteria
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatePasswordCriteriaIcons(s.toString(), icons)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Setup listeners
        dobEditText.setOnClickListener { showDatePicker() }

        // Call the setupLoginListener method to ensure listeners are set up
        setupLoginListener()

        // Initialize other UI elements and listeners
        registerButton.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()
        val dob = dobEditText.text.toString().trim()
        val idNumber = idNumberEditText.text.toString().trim()

        if (validateInput(email, name, surname, dob, idNumber, password)) {
            Log.d("RegisterActivity", "Input validation successful")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("RegisterActivity", "User registration successful")
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        Log.d("RegisterActivity", "Current user ID: $userId")
                        val userData = hashMapOf(
                            "userId" to userId,
                            "email" to email,
                            "name" to name,
                            "surname" to surname,
                            "dob" to dob,
                            "idNumber" to idNumber
                        )
                        saveUserToFirestore(userData)
                    } else {
                        Log.e("RegisterActivity", "Registration failed", task.exception)
                        showToast("Registration failed: ${task.exception?.localizedMessage}")
                    }
                }
        }
    }

    private fun saveUserToFirestore(userData: HashMap<String, String>) {
        val documentId = userData["userId"].toString()
        Log.d("RegisterActivity", "Document ID: $documentId") // Verify document ID

        db.collection("Users").document(documentId)
            .set(userData)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User data saved to Firestore successfully")
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                navigateToLoginActivity()
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Error saving to Firestore", e)
                Toast.makeText(this, "Error saving user: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }


    private fun navigateToLoginActivity() {
        Log.d("RegisterActivity", "Navigating to LoginActivity") // Log when navigation is triggered

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent) // Start the LoginActivity

        Log.d("RegisterActivity", "LoginActivity started") // Confirm the activity has started
        finish() // Close the current activity to prevent back navigation
    }


    private fun initializeViews() {
        emailEditText = findViewById(R.id.email_edit_text)
        nameEditText = findViewById(R.id.name_edit_text)
        surnameEditText = findViewById(R.id.surname_edit_text)
        dobEditText = findViewById(R.id.dob_edit_text)
        idNumberEditText = findViewById(R.id.id_number_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        registerButton = findViewById(R.id.register_button)
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

    private fun validateInput(
        email: String,
        name: String,
        surname: String,
        dob: String,
        idNumber: String,
        password: String
    ): Boolean {
        if (!isValidEmail(email)) {
            showToast("Invalid email address")
            return false
        }

        if (name.isBlank()) {
            showToast("Name cannot be empty")
            return false
        }

        if (surname.isBlank()) {
            showToast("Surname cannot be empty")
            return false
        }

        if (!isValidDate(dob)) {
            showToast("Enter a valid date of birth (yyyy-MM-dd)")
            return false
        }

        if (!isValidIdNumber(idNumber)) {
            showToast("Invalid ID Number")
            return false
        }

        if (!isValidPassword(password)) {
            showToast("Password must be at least 8 characters with a number and a special character")
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
        return emailPattern.matcher(email).matches()
    }

    private fun isValidDate(date: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(date) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidIdNumber(idNumber: String): Boolean {
        return Pattern.matches("\\d{11}", idNumber)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[.!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$")
        return passwordPattern.matcher(password).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun configurePasswordVisibilityToggle() {
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        val toggleButton = findViewById<Button>(R.id.button_show_hide_password)

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

    private fun updatePasswordCriteriaIcons(password: String, icons: List<ImageView>) {
        val criteria = listOf(
            password.length >= 8,
            password.any { it.isDigit() },
            password.any { !it.isLetterOrDigit() },
            password.any { it.isUpperCase() }
        )

        criteria.zip(icons).forEach { (meetsCriteria, icon) ->
            icon.setImageResource(if (meetsCriteria) R.drawable.ic_check_black else R.drawable.ic_cross_black)
        }
    }

    private fun setupLoginListener() {
        val loginTextView = findViewById<TextView>(R.id.textView_login)
        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
