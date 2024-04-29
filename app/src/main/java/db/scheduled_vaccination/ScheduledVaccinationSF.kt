package db.scheduled_vaccination

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun deleteSchedule(schedule_id: Int, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.deleteSchedule(schedule_id)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Schedule deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Schedule deletion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun updateScheduledVaccination(schedule_id: Int, scheduledVaccination: ScheduledVaccination, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.updateScheduledVaccination(schedule_id, scheduledVaccination)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Schedule updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Schedule update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun getScheduleVaccine(schedule_id: Int): ScheduledVaccination? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.getScheduleVaccine(schedule_id)
            connection.close()
            result
        }
    }

    suspend fun getAllScheduleVaccines(): Set<ScheduledVaccination?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val scheduledVaccinationQueries = ScheduledVaccinationQueries(connection)
            val result = scheduledVaccinationQueries.getAllScheduleVaccines()
            connection.close()
            result
        }
    }
}
