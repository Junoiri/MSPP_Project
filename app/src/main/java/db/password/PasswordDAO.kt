package db.password
/**
 * Interface for accessing and managing procedures implemented in php password data in the database.
 */
interface PasswordDAO {

    fun getPassword(password_id: Int): Password?
    fun insertPassword(password: Password): Int
    fun updatePassword(password_id: Int, password: Password): Boolean
    fun deletePassword(password_id: Int): Boolean
}