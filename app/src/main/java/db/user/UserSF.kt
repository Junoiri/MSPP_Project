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
            try {
                val userQueries = UserQueries(connection)
                val result = userQueries.insertUser(user)
                withContext(Dispatchers.Main) {
                    if (result) {
                        Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to create user", Toast.LENGTH_SHORT).show()
                    }
                }
            } finally {
                connection.close()
            }
        }
    }

    suspend fun deleteUser(user_id: Int, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                val result = userQueries.deleteUser(user_id)
                withContext(Dispatchers.Main) {
                    if (result) {
                        Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show()
                    }
                }
            } finally {
                connection.close()
            }
        }
    }

    suspend fun updateUser(user_id: Int, user: User, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            try {
                val userQueries = UserQueries(connection)
                val result = userQueries.updateUser(user_id, user)
                result // Returning the result of updateUser query
            } finally {
                connection.close()
            }
        }.also { result ->
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "User update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



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

