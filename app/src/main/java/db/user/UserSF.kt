package db.user

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  Object class for performing user operations with a SQL database.
 */
object UserSF {

    /**
     * Inserts a new user into the database.
     */
    suspend fun insertUser(user: User, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                val result = userQueries.insertUser(user)
                withContext(Dispatchers.Main) {
                    val message = if (result) "User created" else "Failed to create user"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } finally {
                connection.close()
            }
        }
    }

    /**
     * Deletes a user from the database by its ID.
     */
    suspend fun deleteUser(user_id: Int, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                val result = userQueries.deleteUser(user_id)
                withContext(Dispatchers.Main) {
                    val message = if (result) "User deleted" else "Failed to delete user"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } finally {
                connection.close()
            }
        }
    }

    /**
     * Updates an existing user in the database.
     */
    suspend fun updateUser(user_id: Int, user: User, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                userQueries.updateUser(user_id, user)
            } finally {
                connection.close()
            }
        }.also { result ->
            withContext(Dispatchers.Main) {
                val message = if (result) "User updated" else "User update failed"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Retrieves the ID of a user by their email.
     */
    suspend fun getId(email: String): Int {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                userQueries.getId(email)
            } finally {
                connection.close()
            }
        }
    }

    /**
     * Retrieves a user from the database by its ID.
     */
    suspend fun getUser(user_id: Int): User? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                userQueries.getUser(user_id)
            } finally {
                connection.close()
            }
        }
    }

    /**
     * Retrieves all users from the database.
     */
    suspend fun getAllUsers(): Set<User?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                userQueries.getAllUsers()
            } finally {
                connection.close()
            }
        }
    }

    /**
     * Retrieves the password ID associated with a user's email.
     */
    suspend fun getPasswordId(email: String): Int {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                userQueries.getPasswordId(email)
            } finally {
                connection.close()
            }
        }
    }
}
