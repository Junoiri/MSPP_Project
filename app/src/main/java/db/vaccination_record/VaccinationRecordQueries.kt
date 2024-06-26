package db.vaccination_record

import java.sql.Connection
import java.sql.Date
import java.sql.ResultSet

/**
 * Implementation of functions implemented in DAO for performing vaccination record.
 */
class VaccinationRecordQueries(private val connection: Connection) : VaccinationRecordDAO {

    /**
     * Inserts a new vaccination record into the database.
     */
    override fun insertRecord(vaccinationRecord: VaccinationRecord): Boolean {
        val query = "{CALL insertRecord(?,?,?,?,?,?)}"
        val callableStatement = connection.prepareCall(query)

        callableStatement.setString(1, vaccinationRecord.vaccine_name)
        callableStatement.setDate(2, vaccinationRecord.date_administrated)
        callableStatement.setDate(3, vaccinationRecord.next_dose_due_date)
        callableStatement.setString(4, vaccinationRecord.manufacturer)
        callableStatement.setString(5, vaccinationRecord.dose)
        vaccinationRecord.user_id?.let { callableStatement.setInt(6, it) }

        val result = !callableStatement.execute()
        callableStatement.close()
        return result
    }

    /**
     * Deletes a vaccination record from the database by its ID.
     */
    override fun deleteRecord(record_id: Int): Boolean {
        val query = "{CALL deleteRecord(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, record_id)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Retrieves a vaccination record from the database by its ID.
     */
    override fun getRecord(record_id: Int): VaccinationRecord? {
        val query = "{CALL getRecord(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, record_id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccinationRecord(resultSet)
        } else {
            null
        }
    }

    /**
     * Retrieves all vaccination records for a specific user from the database.
     */
    override fun getAllRecords(user_id: Int): Set<VaccinationRecord?>? {
        val query = "{CALL getAllRecords(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, user_id)
        val resultSet = callableStatement.executeQuery()

        val vaccinations = mutableSetOf<VaccinationRecord?>()
        while (resultSet.next()) {
            vaccinations.add(mapResultSetToVaccinationRecord(resultSet))
        }

        return if (vaccinations.isEmpty()) null else vaccinations
    }

    /**
     * Updates an existing vaccination record in the database.
     */
    override fun updateRecord(record_id: Int, vaccinationRecord: VaccinationRecord): Boolean {
        val query = "{CALL updateRecord(?,?,?,?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        vaccinationRecord.record_id?.let { callableStatement.setInt(1, it) }
        callableStatement.setString(2, vaccinationRecord.vaccine_name)
        callableStatement.setDate(3, vaccinationRecord.date_administrated)
        callableStatement.setDate(4, vaccinationRecord.next_dose_due_date)
        callableStatement.setString(5, vaccinationRecord.manufacturer)
        callableStatement.setString(6, vaccinationRecord.dose)
        vaccinationRecord.user_id?.let { callableStatement.setInt(7, it) }

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Maps a result to record object.
     */
    private fun mapResultSetToVaccinationRecord(resultSet: ResultSet): VaccinationRecord {
        return VaccinationRecord(
            record_id = resultSet.getInt("record_id"),
            vaccine_name = resultSet.getString("vaccine_name"),
            date_administrated = resultSet.getDate("date_administrated"),
            next_dose_due_date = resultSet.getDate("next_dose_due_date"),
            manufacturer = resultSet.getString("manufacturer"),
            dose = resultSet.getString("dose"),
            user_id = resultSet.getInt("user_id")
        )
    }

    /**
     * Retrieves all vaccination records for a specific user on a given date from the database.
     */
    override fun getVaccinationRecordsByDate(user_id: Int, date: Date): Set<VaccinationRecord?>? {
        val query = "{CALL getVaccinationRecordsByDate(?, ?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, user_id)
        callableStatement.setDate(2, date)
        val resultSet = callableStatement.executeQuery()

        val vaccinationRecords = mutableSetOf<VaccinationRecord?>()
        while (resultSet.next()) {
            vaccinationRecords.add(mapResultSetToVaccinationRecord(resultSet))
        }

        return if (vaccinationRecords.isEmpty()) null else vaccinationRecords
    }
}
