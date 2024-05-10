package db.scheduled_vaccination

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date

/**
 * Object for performing scheduled vaccination operations with a SQL database.
 */
object ScheduledVaccinationSF {

    /**
     * Inserts a new scheduled vaccination into the database.
     */
    suspend fun insertSchedule(scheduledVaccination: ScheduledVaccination, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.insertSchedule(scheduledVaccination)
            connection.close()

            // Show toast message based on the insertion result
            withContext(Dispatchers.Main) {
                val message = if (result) "Schedule inserted" else "Schedule insertion failed"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Deletes a scheduled vaccination from the database by its ID.
     */
    suspend fun deleteSchedule(schedule_id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.deleteSchedule(schedule_id)
            connection.close()
            result
        }
    }

    /**
     * Updates an existing scheduled vaccination in the database.
     */
    suspend fun updateScheduledVaccination(schedule_id: Int, scheduledVaccination: ScheduledVaccination): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.updateScheduledVaccination(schedule_id, scheduledVaccination)
            connection.close()

            // Return true if the update was successful, false otherwise
            result
        }
    }

    /**
     * Retrieves scheduled vaccinations for a specific user from the database.
     */
    suspend fun getScheduleVaccine(user_id: Int): Set<ScheduledVaccination?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.getScheduleVaccine(user_id)
            connection.close()
            result
        }
    }

    /**
     * Retrieves scheduled vaccinations for a specific user on a given date from the database.
     */
    suspend fun getScheduledVaccinationsByDate(user_id: Int, date: Date): Set<ScheduledVaccination?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.getScheduledVaccinationsByDate(user_id, date)
            connection.close()
            result
        }
    }
}
