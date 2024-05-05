package db.scheduled_vaccination

import java.sql.Date

data class ScheduledVaccination(
    val schedule_id: Int? =null,
    val vaccine_name: String?=null,
    val schedule_date: Date?=null,
    val manufacturer: String?=null,
    val dose: String?=null,
)
