package db.user
/**
 * Interface for accessing and managing PHP user related procedures in the database.
 */
interface UserDAO {
    fun getUser(user_id: Int): User?
    fun getAllUsers(): Set<User?>?
    fun insertUser(user: User): Boolean
    fun deleteUser(user_id: Int): Boolean
    fun updateUser(user_id: Int, user:User): Boolean
    fun getId(email:String): Int
    fun getPasswordId(email:String): Int
}