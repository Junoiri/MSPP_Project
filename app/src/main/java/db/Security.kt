package db
import java.sql.Connection

class Security (private val connection: Connection) {

    private fun getHashedPasswordByEmail(email: String): String? {
        val query = "SELECT password FROM users WHERE email = ?"
        connection.prepareStatement(query).use { preparedStatement ->
            preparedStatement.setString(1, email)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return resultSet.getString("password")
            }
        }
        return null
    }
}