package utils

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarHelper {
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