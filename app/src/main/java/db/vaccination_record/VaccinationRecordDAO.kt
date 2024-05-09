package db.vaccination_record

interface VaccinationRecordDAO {
    fun getRecord(user_id: Int): Set<VaccinationRecord?>?
    fun insertRecord(vaccinationRecord: VaccinationRecord): Boolean
    fun deleteRecord(record_id: Int): Boolean
    fun updateRecord(record_id: Int, vaccinationRecord: VaccinationRecord): Boolean
}