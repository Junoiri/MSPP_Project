package db.password

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Performing password-related operations with a SQL database.
 */
object PasswordSF {

    /**
     * Inserts a new password into the database.
     */
    suspend fun insertPassword(password: Password, context: Context): Int {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val passwordQueries = PasswordQueries(connection)
            val result = passwordQueries.insertPassword(password)
            connection.close()

            // Show toast message based on the insertion result
            withContext(Dispatchers.Main) {
                val message = if (result != -1) "Password inserted" else "Failed to insert password"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            result
        }
    }

    /**
     * Updates an existing password in the database.
     */
    suspend fun updatePassword(password_id: Int, newPassword: String, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val passwordQueries = PasswordQueries(connection)
            val result = passwordQueries.updatePassword(password_id, Password(password_id, newPassword))
            connection.close()

            // Show toast message based on the update result
            withContext(Dispatchers.Main) {
                val message = if (result) "Password updated" else "Failed to update password"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            result
        }
    }

    /**
     * Deletes a password from the database.
     */
    suspend fun deletePassword(password_id: Int, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val passwordQueries = PasswordQueries(connection)
            val result = passwordQueries.deletePassword(password_id)
            connection.close()

            // Show toast message based on the deletion result
            withContext(Dispatchers.Main) {
                val message = if (result) "Password deleted" else "Password deletion failed"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            result
        }
    }

    /**
     * Retrieves a password from the database by its ID.
     */
    suspend fun getPassword(password_id: Int): Password? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val passwordQueries = PasswordQueries(connection)
            val result = passwordQueries.getPassword(password_id)
            connection.close()
            result
        }
    }
}
