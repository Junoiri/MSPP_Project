package db.scheduled_vaccination
import java.sql.Connection
import java.sql.ResultSet

class ScheduledVaccinationQueries(private val connection: Connection): ScheduledVaccinationDAO {

    override fun insertSchedule(scheduledVaccination: ScheduledVaccination): Boolean {
        val query = "{CALL insertSchedule(?,?,?,?)}"
        val callableStatement= connection.prepareCall(query)

        callableStatement.setString(1,scheduledVaccination.vaccine_name)
        callableStatement.setDate(2,scheduledVaccination.schedule_date)
        callableStatement.setString(3,scheduledVaccination.manufacturer)
        callableStatement.setString(4,scheduledVaccination.dose)

        val result = !callableStatement.execute()
        callableStatement.close()
        return result
    }
    override fun deleteSchedule(schedule_id: Int): Boolean {
        val query= "{CALL deleteSchedule(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1,schedule_id)

        return callableStatement.executeUpdate() > 0
    }
    override fun getScheduleVaccine(schedule_id: Int): ScheduledVaccination? {
        val query= "{CALL getScheduleVaccine(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1,schedule_id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()){
            mapResultSetToScheduledVaccination(resultSet)
        }else{
            null
        }
    }
    override fun getAllScheduleVaccines(): Set<ScheduledVaccination?>? {
        val query= "{CALL getAllScheduleVaccines()}"
        val callableStatement= connection.prepareCall(query)
        val resultSet= callableStatement.executeQuery()

        val vaccinations= mutableSetOf<ScheduledVaccination?>()
        while (resultSet.next()){
            vaccinations.add(mapResultSetToScheduledVaccination(resultSet))
        }

        return if (vaccinations.isEmpty()) null else vaccinations
    }

    override fun updateScheduledVaccination(schedule_id: Int, scheduledVaccination: ScheduledVaccination): Boolean {
        val query = "{CALL updateScheduledVaccination(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, scheduledVaccination.vaccine_name)
        callableStatement.setDate(2, scheduledVaccination.schedule_date)
        callableStatement.setString(3, scheduledVaccination.manufacturer)
        callableStatement.setString(4, scheduledVaccination.dose)

        return callableStatement.executeUpdate() > 0
    }

    private fun mapResultSetToScheduledVaccination(resultSet: ResultSet): ScheduledVaccination {
        return ScheduledVaccination(
            schedule_id= resultSet.getInt("schedule_id"),
            vaccine_name = resultSet.getString("vaccine_name"),
            schedule_date = resultSet.getDate("schedule_date"),
            manufacturer = resultSet.getString("manufacturer"),
            dose = resultSet.getString("dose"),

        )
    }


}