import android.view.View
import com.example.mspp_project.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    fun showCustomSnackbar(view: View, message: String, duration: Int) {
        val snackbar = Snackbar.make(view, message, duration)
        snackbar.view.setBackgroundResource(R.style.CustomSnackbar) // Apply the custom style
        snackbar.show()
    }
}
