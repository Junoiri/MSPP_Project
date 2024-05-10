package db.scheduled_vaccination

import java.sql.Date

interface ScheduledVaccinationDAO {
    fun getScheduleVaccine(user_id: Int): Set<ScheduledVaccination?>?
    fun insertSchedule(scheduledVaccination: ScheduledVaccination): Boolean
    fun deleteSchedule(schedule_id: Int): Boolean
    fun updateScheduledVaccination(schedule_id: Int, scheduledVaccination: ScheduledVaccination): Boolean
    fun getScheduledVaccinationsByDate(user_id: Int, date: Date): Set<ScheduledVaccination?>?
}