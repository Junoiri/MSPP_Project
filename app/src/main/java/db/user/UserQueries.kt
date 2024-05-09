package db.user

import java.sql.Connection
import java.sql.ResultSet

class UserQueries(private val connection: Connection): UserDAO {

    override fun insertUser(user: User): Boolean {
        val query = "{CALL insertUser(?,?,?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, user.name)
        callableStatement.setString(2, user.surname)
        callableStatement.setString(3, user.email)
        callableStatement.setDate(4, user.date_of_birth)
        user.password_id?.let { callableStatement.setInt(5, it) }

        val result = !callableStatement.execute()
        callableStatement.close()
        return result
    }

    override fun deleteUser(user_id: Int): Boolean {
        val query= "{CALL deleteUser(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1,user_id)

        return callableStatement.executeUpdate() > 0
    }

    override fun getUser(user_id: Int): User? {
        val query= "{CALL getUser(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1,user_id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()){
            mapResultSetToUser(resultSet)
        }else{
            null
        }
    }

    override fun getAllUsers(): Set<User?>? {
        val query= "{CALL getAllUsers()}"
        val callableStatement= connection.prepareCall(query)
        val resultSet= callableStatement.executeQuery()

        val users= mutableSetOf<User?>()
        while (resultSet.next()){
            users.add(mapResultSetToUser(resultSet))
        }

        return if (users.isEmpty()) null else users
    }

    override fun updateUser(user_id: Int, user: User): Boolean {
        val query = "{CALL updateUser(?,?,?,?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, user_id)
        callableStatement.setString(2, user.name)
        callableStatement.setString(3, user.surname)
        callableStatement.setString(4, user.email)
        callableStatement.setDate(5, user.date_of_birth)
        user.password_id?.let { callableStatement.setInt(6, it) }

        return callableStatement.executeUpdate() > 0
    }

    override fun getId(email: String): Int {
        val query = "{CALL getId(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, email)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getInt("user_id")
        } else {
            -1
        }
    }
    override fun getPasswordId(email: String): Int {
        val query = "{CALL getPasswordId(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, email)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getInt("password_id")
        } else {
            -1
        }
    }

    private fun mapResultSetToUser(resultSet: ResultSet): User {
        return User(
            user_id= resultSet.getInt("user_id"),
            name= resultSet.getString("name"),
            surname= resultSet.getString("surname"),
            email= resultSet.getString("email"),
            date_of_birth = resultSet.getDate("date_of_birth"),
            password_id = resultSet.getInt("password_id")
        )
    }
}

