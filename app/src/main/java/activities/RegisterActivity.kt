package activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mspp_project.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var dobEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val passwordEditText: EditText = findViewById(R.id.password_edit_text)
        configurePasswordVisibilityToggle()

        val icons = listOf(
            findViewById<ImageView>(R.id.password_criteria_1_icon),
            findViewById<ImageView>(R.id.password_criteria_2_icon),
            findViewById<ImageView>(R.id.password_criteria_3_icon),
            findViewById<ImageView>(R.id.password_criteria_4_icon)
        )

        val nameEditText: EditText = findViewById(R.id.name_edit_text)
        val surnameEditText: EditText = findViewById(R.id.surname_edit_text)
        dobEditText = findViewById(R.id.dob_edit_text)
        val idNumberEditText: EditText = findViewById(R.id.id_number_edit_text)
        val registerButton: Button = findViewById(R.id.register_button)

        dobEditText.setOnClickListener {
            showDatePickerDialog()
        }

        configurePasswordVisibilityToggle()

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updatePasswordCriteriaIcons(s.toString(), icons)
            }

            override fun afterTextChanged(s: Editable) {}
        })


        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val idNumber = idNumberEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(name, surname, dob, idNumber, password)) {
                // Input is valid, proceed with the registration logic
                // TODO: Implement your registration logic here
            }
        }

        val loginTextView: TextView = findViewById(R.id.textView_login)
        loginTextView.setOnClickListener {
            // Start LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dobEditText.setText(dateFormat.format(selectedDate.time))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun validateInput(name: String, surname: String, dob: String, idNumber: String, password: String): Boolean {
        // Validate name
        if (name.isBlank()) {
            showToast("Name cannot be empty")
            return false
        }

        // Validate surname
        if (surname.isBlank()) {
            showToast("Surname cannot be empty")
            return false
        }

        // Validate date of birth with a date format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false
        try {
            dateFormat.parse(dob) ?: throw IllegalArgumentException()
        } catch (e: Exception) {
            showToast("Enter a valid date of birth (yyyy-MM-dd)")
            return false
        }

        // Validate ID number (previously known as PESEL)
        if (!isValidIdNumber(idNumber)) {
            showToast("Invalid ID Number")
            return false
        }

        // Validate password
        val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[.!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$")
        if (!passwordPattern.matcher(password).matches()) {
            showToast("Password must be at least 8 characters including a number and a special character")
            return false
        }

        return true
    }

    private fun isValidIdNumber(idNumber: String): Boolean {
        if (!Pattern.matches("\\d{11}", idNumber)) {
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun configurePasswordVisibilityToggle() {
        val passwordEditText: EditText = findViewById(R.id.password_edit_text)
        val toggleButton: Button = findViewById(R.id.button_show_hide_password)

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
}