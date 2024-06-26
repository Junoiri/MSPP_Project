package db.scheduled_vaccination

import java.sql.Connection
import java.sql.Date
import java.sql.ResultSet

/**
 * Implementation of functions for performing scheduled vaccination operations in the database.
 */
class ScheduledVaccinationQueries(private val connection: Connection) : ScheduledVaccinationDAO {

    /**
     * Inserts a new scheduled vaccination into the database.
     */
    override fun insertSchedule(scheduledVaccination: ScheduledVaccination): Boolean {
        val query = "{CALL insertSchedule(?,?,?,?,?,?)}"
        val callableStatement = connection.prepareCall(query)

        callableStatement.setString(1, scheduledVaccination.vaccine_name)
        callableStatement.setDate(2, scheduledVaccination.schedule_date)
        callableStatement.setString(3, scheduledVaccination.manufacturer)
        callableStatement.setString(4, scheduledVaccination.dose)
        scheduledVaccination.user_id?.let { callableStatement.setInt(5, it) }

        val result = !callableStatement.execute()
        callableStatement.close()
        return result
    }

    /**
     * Deletes a scheduled vaccination from the database by its ID.
     */
    override fun deleteSchedule(schedule_id: Int): Boolean {
        val query = "{CALL deleteSchedule(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, schedule_id)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Gets scheduled vaccinations for a specific user from the database.
     */
    override fun getScheduleVaccine(user_id: Int): Set<ScheduledVaccination?>? {
        val query = "{CALL getScheduleVaccineByUserId(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, user_id)
        val resultSet = callableStatement.executeQuery()

        val vaccinations = mutableSetOf<ScheduledVaccination?>()
        while (resultSet.next()) {
            vaccinations.add(mapResultSetToScheduledVaccination(resultSet))
        }

        return if (vaccinations.isEmpty()) null else vaccinations
    }

    /**
     * Updates an existing scheduled vaccination in the database.
     */
    override fun updateScheduledVaccination(schedule_id: Int, scheduledVaccination: ScheduledVaccination): Boolean {
        val query = "{CALL updateScheduledVaccination(?,?,?,?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        scheduledVaccination.schedule_id?.let { callableStatement.setInt(1, it) }
        callableStatement.setString(2, scheduledVaccination.vaccine_name)
        callableStatement.setDate(3, scheduledVaccination.schedule_date)
        callableStatement.setString(4, scheduledVaccination.manufacturer)
        callableStatement.setString(5, scheduledVaccination.dose)
        scheduledVaccination.user_id?.let { callableStatement.setInt(6, it) }

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Retrieves scheduled vaccinations for a specific user on a given date from the database.
     */
    override fun getScheduledVaccinationsByDate(user_id: Int, date: Date): Set<ScheduledVaccination?>? {
        val query = "{CALL getScheduledVaccinationsByDate(?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, user_id)
        callableStatement.setDate(2, date)
        val resultSet = callableStatement.executeQuery()

        val scheduledVaccinations = mutableSetOf<ScheduledVaccination?>()
        while (resultSet.next()) {
            scheduledVaccinations.add(mapResultSetToScheduledVaccination(resultSet))
        }

        return if (scheduledVaccinations.isEmpty()) null else scheduledVaccinations
    }

    /**
     * Maps a result to a ScheduledVaccination object.
     */
    private fun mapResultSetToScheduledVaccination(resultSet: ResultSet): ScheduledVaccination {
        return ScheduledVaccination(
            schedule_id = resultSet.getInt("schedule_id"),
            vaccine_name = resultSet.getString("vaccine_name"),
            schedule_date = resultSet.getDate("schedule_date"),
            manufacturer = resultSet.getString("manufacturer"),
            dose = resultSet.getString("dose"),
            user_id = resultSet.getInt("user_id")
        )
    }
}
