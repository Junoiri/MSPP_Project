package authenticators

import activities.MainActivity
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * This class is responsible for managing Google authentication.
 *
 * @property activity The activity in which this manager is operating.
 * @property clientId The client ID for the Google Sign-In.
 */
class GoogleAuthManager(private val activity: AppCompatActivity, private val clientId: String) {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val activityResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val signInAccount = accountTask.getResult(ApiException::class.java)
                    val authCredential =
                        GoogleAuthProvider.getCredential(signInAccount?.idToken, null)
                    auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity, "Signed in successfully!", Toast.LENGTH_SHORT)
                                .show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(activity, MainActivity::class.java)
                                activity.startActivity(intent)
                                activity.finish()
                            }, 1000) // 1 seconds delay
                        } else {
                            Toast.makeText(
                                activity,
                                "Failed to sign in: " + task.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }

    /**
     * Initializes the FirebaseAuth instance, GoogleSignInClient, and sets up the activity result launcher.
     */
    init {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, options)
        auth = FirebaseAuth.getInstance()
    }

    /**
     * Initiates the sign in process with Google.
     */
    fun signIn() {
        val intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(intent)
    }
}