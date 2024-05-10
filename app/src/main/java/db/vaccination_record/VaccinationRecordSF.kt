package db.vaccination_record

import android.content.Context
import android.widget.Toast
import db.DConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object VaccinationRecordSF {

    suspend fun insertRecord(vaccinationRecord: VaccinationRecord, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.insertRecord(vaccinationRecord)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Record inserted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record insertion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun deleteRecord(record_id: Int, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.deleteRecord(record_id)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record deletion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun updateRecord(record_id: Int, vaccinationRecord: VaccinationRecord, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.updateRecord(record_id, vaccinationRecord)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Record updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Record update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun getRecord(record_id: Int): VaccinationRecord? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.getRecord(record_id)
            connection.close()
            result
        }
    }

    suspend fun getAllRecords(user_id: Int): Set<VaccinationRecord?>? {
        return withContext(Dispatchers.IO) {
            val connection = DConnection.getConnection()
            val vaccinationRecordQueries = VaccinationRecordQueries(connection)
            val result = vaccinationRecordQueries.getAllRecords(user_id)
            connection.close()
            result
        }
    }

}
