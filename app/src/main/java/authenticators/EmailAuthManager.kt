package authenticators
import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult

class EmailAuthManager(private val auth: FirebaseAuth, private val context: Context) {

    // Function to sign in with email and password
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
