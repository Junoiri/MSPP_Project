package utils

import android.content.Context
import android.widget.Toast

/**
 * This object is responsible for managing toasts.
 * Future improvement - reorganize all the toast messages into this object.
 * */
object ToastHelper {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}