package db.user
/**
 * Represents a user entry in the database.
 */

data class User(
    val user_id: Int? = null,
    val name: String?= null,
    val surname: String?= null,
    val email: String?= null,
    val date_of_birth: java.sql.Date?=null,
    val password_id: Int? = null,
)
