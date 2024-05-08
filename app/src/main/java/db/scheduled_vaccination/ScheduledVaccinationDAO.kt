package db.scheduled_vaccination

interface ScheduledVaccinationDAO {
    fun getScheduleVaccine(user_id: Int): Set<ScheduledVaccination?>?
    fun insertSchedule(scheduledVaccination: ScheduledVaccination): Boolean
    fun deleteSchedule(schedule_id: Int): Boolean
    fun updateScheduledVaccination(schedule_id: Int, scheduledVaccination: ScheduledVaccination): Boolean
}