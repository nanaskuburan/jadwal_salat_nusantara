package com.example.data

import kotlinx.coroutines.flow.Flow

class WorshipRepository(private val worshipDao: WorshipDao) {
    fun getRecordByDate(date: String): Flow<WorshipRecord?> = worshipDao.getRecordByDate(date)

    val allRecords: Flow<List<WorshipRecord>> = worshipDao.getAllRecords()

    suspend fun saveRecord(record: WorshipRecord) {
        worshipDao.insertOrUpdate(record)
    }

    suspend fun deleteRecord(date: String) {
        worshipDao.deleteByDate(date)
    }
}
