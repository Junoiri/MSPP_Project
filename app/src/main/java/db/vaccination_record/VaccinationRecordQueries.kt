package db.vaccination_record

import java.sql.Connection
import java.sql.ResultSet

class VaccinationRecordQueries(private val connection: Connection): VaccinationRecordDAO {

    override fun insertRecord(vaccinationRecord: VaccinationRecord): Boolean {
        val query = "{CALL insertSchedule(?,?,?,?,?)}"
        val callableStatement= connection.prepareCall(query)

        callableStatement.setString(1,vaccinationRecord.vaccine_name)
        callableStatement.setDate(2,vaccinationRecord.date_administrated)
        callableStatement.setDate(3,vaccinationRecord.next_dose_due_date)
        callableStatement.setString(4,vaccinationRecord.manufacturer)
        callableStatement.setString(5,vaccinationRecord.dose)

        val result = !callableStatement.execute()
        callableStatement.close()
        return result
    }
    override fun deleteRecord(record_id: Int): Boolean {
        val query= "{CALL deleteRecord(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1,record_id)

        return callableStatement.executeUpdate() > 0
    }
    override fun getRecord(record_id: Int): VaccinationRecord? {
        val query= "{CALL getVaccinationRecord(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1,record_id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()){
            mapResultSetToVaccinationRecord(resultSet)
        }else{
            null
        }
    }
    override fun getAllRecords(): Set<VaccinationRecord?>? {
        val query= "{CALL getAllRecords()}"
        val callableStatement= connection.prepareCall(query)
        val resultSet= callableStatement.executeQuery()

        val vaccinations= mutableSetOf<VaccinationRecord?>()
        while (resultSet.next()){
            vaccinations.add(mapResultSetToVaccinationRecord(resultSet))
        }

        return if (vaccinations.isEmpty()) null else vaccinations
    }
    override fun updateRecord(record_id: Int, vaccinationRecord: VaccinationRecord): Boolean {
        val query = "{CALL updateRecord(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1,vaccinationRecord.vaccine_name)
        callableStatement.setDate(2,vaccinationRecord.date_administrated)
        callableStatement.setDate(3,vaccinationRecord.next_dose_due_date)
        callableStatement.setString(4,vaccinationRecord.manufacturer)
        callableStatement.setString(5,vaccinationRecord.dose)

        return callableStatement.executeUpdate() > 0
    }
    private fun mapResultSetToVaccinationRecord(resultSet: ResultSet): VaccinationRecord {
        return VaccinationRecord(
            vaccine_name = resultSet.getString("vaccine_name"),
            date_administrated = resultSet.getDate("date_administrated"),
            next_dose_due_date = resultSet.getDate("next_dose_due_date"),
            manufacturer = resultSet.getString("manufacturer"),
            dose = resultSet.getString("dose"),
            )
    }
}