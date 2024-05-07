package db.user
import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserSF {

    suspend fun insertUser(user: User, context: Context) {
        val connection = DConnection.getConnection()
        try {
            withContext(Dispatchers.IO) {
                val userQueries = UserQueries(connection)
                val result = userQueries.insertUser(user)
                result
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } finally {
            connection.close()
        }
    }

    suspend fun deleteUser(user_id: Int, context: Context) {
        val connection = DConnection.getConnection()
        try {
            withContext(Dispatchers.IO) {
                val userQueries = UserQueries(connection)
                val result = userQueries.deleteUser(user_id)
                result
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } finally {
            connection.close()
        }
    }

    suspend fun updateEmail(user_id: Int, user: User, context: Context) {
        val connection = DConnection.getConnection()
        try {
            withContext(Dispatchers.IO) {
                val userQueries = UserQueries(connection)
                val result = userQueries.updateUser(user_id, user)
                result
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } finally {
            connection.close()
        }
    }

    suspend fun getUser(user_id: Int): User? {
        val connection = DConnection.getConnection()
        return try {
            withContext(Dispatchers.IO) {
                val userQueries = UserQueries(connection)
                userQueries.getUser(user_id)
            }
        } catch (e: Exception) {
            null
        } finally {
            connection.close()
        }
    }

    suspend fun getAllUsers(): Set<User?>? {
        val connection = DConnection.getConnection()
        return try {
            withContext(Dispatchers.IO) {
                val userQueries = UserQueries(connection)
                userQueries.getAllUsers()
            }
        } catch (e: Exception) {
            null
        } finally {
            connection.close()
        }
    }
}
