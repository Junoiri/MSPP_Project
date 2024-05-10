package db.vaccination_record
/**
 * Represents a vaccination record entry in the database.
 */

data class VaccinationRecord(
    val record_id: Int?=null,
    val vaccine_name: String?=null,
    val date_administrated: java.sql.Date?=null,
    val next_dose_due_date: java.sql.Date?=null,
    val manufacturer: String?=null,
    val dose: String?=null,
    var user_id: Int? = null,
)
