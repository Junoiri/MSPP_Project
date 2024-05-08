package db.password

import java.security.MessageDigest
import java.sql.Connection
import java.sql.ResultSet

class PasswordQueries(private val connection: Connection) : PasswordDAO {

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
            // After inserting the password, fetch the last inserted passwordId
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

    override fun deletePassword(password_id: Int): Boolean {
        val query = "{CALL deletePassword(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, password_id)

        return callableStatement.executeUpdate() > 0
    }

    private fun mapResultSetToPassword(resultSet: ResultSet): Password {
        return Password(
            password_id = resultSet.getInt("password_id"),
            password = resultSet.getString("password")
        )
    }

    private fun hash(input: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-1")
            .digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
