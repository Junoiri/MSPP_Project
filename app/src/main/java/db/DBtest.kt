package db

import db.scheduled_vaccination.ScheduledVaccinationQueries
import db.user.User
import db.user.UserQueries
import db.vaccination_record.VaccinationRecordQueries
import java.sql.Connection
import java.sql.Date
import java.text.SimpleDateFormat

object DBtest {
    @JvmStatic
    fun main(args: Array<String>) {
        var connection: Connection? = null
        try {
            connection = DConnection.getConnection()
            val userQueries = UserQueries(connection)
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)

            println("Testing insertUser():")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dateOfBirth = Date(dateFormat.parse("2005-01-20").time)

            val newUser = User(
                name = "Anna",
                surname = "Doe",
                email = "anna@doe.com",
                date_of_birth = dateOfBirth
            )

            println("User insertion successful: ${userQueries.insertUser(newUser)}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.close()
        }
    }
}
