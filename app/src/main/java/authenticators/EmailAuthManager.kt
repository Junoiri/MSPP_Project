package authenticators

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult

/**
 * This class is responsible for managing email authentication.
 *
 * @property auth The FirebaseAuth instance.
 * @property context The context in which this manager is operating.
 */
class EmailAuthManager(private val auth: FirebaseAuth, private val context: Context) {
    /**
     * Signs in with the provided email and password.
     *
     * @param email The email to sign in with.
     * @param password The password to sign in with.
     * @param onSuccess The function to call when the sign in is successful. The current Firebase user is passed as an argument.
     * @param onFailure The function to call when the sign in fails. The error message is passed as an argument.
     */
    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)  // Login successful
                } else {
                    onFailure("Authentication failed: ${task.exception?.localizedMessage}")
                }
            }
    }
}
