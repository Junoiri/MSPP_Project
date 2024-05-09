package db.password

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PasswordSF {

    suspend fun insertPassword(password: Password, context: Context): Int {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val passwordQueries = PasswordQueries(connection)
            val result = passwordQueries.insertPassword(password)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result != -1) {
                    Toast.makeText(context, "Password inserted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to insert password", Toast.LENGTH_SHORT).show()
                }
            }

            result
        }
    }

    suspend fun updatePassword(password_id: Int, newPassword: String, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val passwordQueries = PasswordQueries(connection)
            val result = passwordQueries.updatePassword(password_id, Password(password_id, newPassword))
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }

            result
        }
    }

    suspend fun deletePassword(password_id: Int, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val passwordQueries = PasswordQueries(connection)
            val result = passwordQueries.deletePassword(password_id)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Password deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Password deletion failed", Toast.LENGTH_SHORT).show()
                }
            }

            result
        }
    }

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
