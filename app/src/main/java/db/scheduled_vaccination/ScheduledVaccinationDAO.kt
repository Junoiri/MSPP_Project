package db.scheduled_vaccination

import db.user.User

interface ScheduledVaccinationDAO {
    fun getScheduleVaccine(schedule_id: Int): ScheduledVaccination?
    fun getAllScheduleVaccines(): Set<ScheduledVaccination?>?
    fun insertSchedule(scheduledVaccination: ScheduledVaccination): Boolean
    fun deleteSchedule(schedule_id: Int): Boolean
    fun updateScheduledVaccination(schedule_id: Int, scheduledVaccination: ScheduledVaccination): Boolean
}