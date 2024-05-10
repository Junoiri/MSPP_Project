package utils

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * This object is responsible for managing snackbars.
 */
object SnackbarHelper {
    /**
     * Shows a snackbar asking the user if they want to save changes.
     *
     * @param activity The activity in which this manager is operating.
     * @param yesAction The action to perform when the user clicks "Yes".
     * @param noAction The action to perform when the snackbar is dismissed without action.
     */
    fun showSaveChangesSnackbar(
        activity: Activity,
        yesAction: () -> Unit,
        noAction: () -> Unit
    ) {
        val snackbar = Snackbar.make(
            activity.findViewById(android.R.id.content),
            "Do you want to save changes?",
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction("Yes") { yesAction() }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    noAction()
                }
            }
        })

        snackbar.show()
    }
}