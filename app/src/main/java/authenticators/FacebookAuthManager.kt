package authenticators

import activities.MainActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

/**
 * This class is responsible for managing Facebook authentication.
 *
 * @property activity The activity in which this manager is operating.
 */
class FacebookAuthManager(private val activity: AppCompatActivity) {
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    /**
     * Initializes the FirebaseAuth instance, CallbackManager, and registers a callback for the LoginManager.
     */
    init {
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken.token)
                }

                override fun onCancel() {
                    Toast.makeText(activity, "Facebook sign in cancelled", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(activity, "An error occurred: $error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    /**
     * Initiates the sign in process with Facebook.
     */
    fun signIn() {
        LoginManager.getInstance()
            .logInWithReadPermissions(activity, listOf("email", "public_profile"))
    }

    /**
     * Handles the Facebook access token.
     *
     * @param token The Facebook access token.
     */
    private fun handleFacebookAccessToken(token: String) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Signed in successfully!", Toast.LENGTH_SHORT).show()
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
    }

    /**
     * Handles the result from the Facebook sign in activity.
     *
     * @param requestCode The request code originally supplied to startActivityForResult().
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}