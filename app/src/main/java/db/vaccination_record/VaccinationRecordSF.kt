package db.vaccination_record

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date

/**
 * Provides methods for managing vaccination functions.
 */
object VaccinationRecordSF {

    /**
     * Inserts a new vaccination record into the database.
     */
    suspend fun insertRecord(vaccinationRecord: VaccinationRecord, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.insertRecord(vaccinationRecord)
            connection.close()

            // Show a toast message based on the insertion result
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Record inserted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record insertion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Deletes a vaccination record from the database by its ID.
     */
    suspend fun deleteRecord(record_id: Int, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.deleteRecord(record_id)
            connection.close()

            // Show a toast message based on the deletion result
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record deletion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Updates a vaccination record in the database.
     */
    suspend fun updateRecord(record_id: Int, vaccinationRecord: VaccinationRecord, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.updateRecord(record_id, vaccinationRecord)
            connection.close()

            // Show a toast message based on the update result
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Record updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Retrieves a vaccination record from the database by its ID.
     */
    suspend fun getRecord(record_id: Int): VaccinationRecord? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.getRecord(record_id)
            connection.close()
            result
        }
    }

    /**
     * Retrieves all vaccination records for a specific user from the database.
     */
    suspend fun getAllRecords(user_id: Int): Set<VaccinationRecord?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.getAllRecords(user_id)
            connection.close()
            result
        }
    }

    /**
     * Retrieves all vaccination records for a specific user on a given date from the database.
     */
    suspend fun getVaccinationRecordsByDate(user_id: Int, date: Date): Set<VaccinationRecord?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.getVaccinationRecordsByDate(user_id, date)
            connection.close()
            result
        }
    }
}
