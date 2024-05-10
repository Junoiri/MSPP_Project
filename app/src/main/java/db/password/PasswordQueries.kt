package db.password

import java.security.MessageDigest
import java.sql.Connection
import java.sql.ResultSet

/**
 * Implementation of functions from passwordDAO for performing operations in the database
 */
class PasswordQueries(private val connection: Connection) : PasswordDAO {

    /**
     * Retrieves a password from the database by its ID.
     * Returns the Password
     */
    override fun getPassword(password_id: Int): Password? {
        val query = "{CALL getPassword(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, password_id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToPassword(resultSet)
        } else {
            null
        }
    }

    /**
     * Inserts a new password into the database
     * Return the ID of the inserted password
     */
    override fun insertPassword(password: Password): Int {
        val query = "{CALL insertPassword(?)}"
        val callableStatement = connection.prepareCall(query)

        val hashedPassword = password.password?.let {
            hash(it)
        } ?: ""

        callableStatement.setString(1, hashedPassword)

        val result = callableStatement.executeUpdate()
        var passwordId = -1

        if (result > 0) {
            // Fetch the last inserted passwordId
            val selectLastIdQuery = "SELECT LAST_INSERT_ID() AS last_id"
            val selectStatement = connection.createStatement()
            val resultSet = selectStatement.executeQuery(selectLastIdQuery)

            if (resultSet.next()) {
                passwordId = resultSet.getInt("last_id")
            }
            selectStatement.close()
        }

        callableStatement.close()

        return passwordId
    }

    /**
     * Updates an existing password in the database
     * Takes the password_id and password, then update it in the database
     */
    override fun updatePassword(password_id: Int, password: Password): Boolean {
        val hashedPassword = password.password?.let {
            hash(it)
        } ?: ""

        val query = "{CALL updatePassword(?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, password_id)
        callableStatement.setString(2, hashedPassword)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Taking the password_id and deletes it from the database.
     */
    override fun deletePassword(password_id: Int): Boolean {
        val query = "{CALL deletePassword(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, password_id)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Maps a Result to a object.
     */
    private fun mapResultSetToPassword(resultSet: ResultSet): Password {
        return Password(
            password_id = resultSet.getInt("password_id"),
            password = resultSet.getString("password")
        )
    }

    /**
     * Hashes the input string using SHA-1 algorithm, returns a hashed string password
     */
    private fun hash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
