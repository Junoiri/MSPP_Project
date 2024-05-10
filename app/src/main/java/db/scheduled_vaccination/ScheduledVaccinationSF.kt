package db.scheduled_vaccination

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date

object ScheduledVaccinationSF {

    suspend fun insertSchedule(scheduledVaccination: ScheduledVaccination, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.insertSchedule(scheduledVaccination)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Schedule inserted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Schedule insertion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun deleteSchedule(schedule_id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.deleteSchedule(schedule_id)
            connection.close()
            result
        }
    }


    suspend fun updateScheduledVaccination(
        schedule_id: Int,
        scheduledVaccination: ScheduledVaccination
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.updateScheduledVaccination(schedule_id, scheduledVaccination)
            connection.close()

            // Return true if the update was successful, false otherwise
            result
        }
    }


    suspend fun getScheduleVaccine(user_id: Int): Set<ScheduledVaccination?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.getScheduleVaccine(user_id)
            connection.close()
            result
        }
    }
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
