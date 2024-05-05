package db.user
import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserSF {

    suspend fun insertUser(user: User, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val userQueries = UserQueries(connection)
            val result = userQueries.insertUser(user)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "User creation failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

suspend fun deleteUser(user_id: Int, context: Context) {
    withContext(Dispatchers.IO) {
        val connection = DConnection.getConnection()
        val userQueries = UserQueries(connection)
        val result = userQueries.deleteUser(user_id)
        connection.close()

        withContext(Dispatchers.Main) {
            if (result) {
                Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "User deletion failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
suspend fun updateEmail(user_id: Int, user: User, context: Context) {
    withContext(Dispatchers.IO) {
        val connection = DConnection.getConnection()
        val userQueries = UserQueries(connection)
        val result = userQueries.updateUser(user_id, user)
        connection.close()

        withContext(Dispatchers.Main) {
            if (result) {
                Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "User update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

suspend fun getUser(user_id: Int): User? {
    return withContext(Dispatchers.IO) {
        val connection = DConnection.getConnection()
        val userQueries = UserQueries(connection)
        val result = userQueries.getUser(user_id)
        connection.close()
        result
    }
}
suspend fun getAllUsers(): Set<User?>? {
    return withContext(Dispatchers.IO) {
        val connection = DConnection.getConnection()
        val userQueries = UserQueries(connection)
        val result = userQueries.getAllUsers()
        connection.close()

        result
    }
}
}