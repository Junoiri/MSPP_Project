package db.vaccination_record

import java.sql.Date

interface VaccinationRecordDAO {
    fun getRecord(record_id: Int): VaccinationRecord?
    fun getAllRecords(user_id: Int): Set<VaccinationRecord?>?
    fun insertRecord(vaccinationRecord: VaccinationRecord): Boolean
    fun deleteRecord(record_id: Int): Boolean
    fun updateRecord(record_id: Int, vaccinationRecord: VaccinationRecord): Boolean
    fun getVaccinationRecordsByDate(user_id: Int, date: Date): Set<VaccinationRecord?>?
}