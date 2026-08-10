package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorshipDao {
    @Query("SELECT * FROM worship_records WHERE date = :date")
    fun getRecordByDate(date: String): Flow<WorshipRecord?>

    @Query("SELECT * FROM worship_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<WorshipRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: WorshipRecord)

    @Query("DELETE FROM worship_records WHERE date = :date")
    suspend fun deleteByDate(date: String)
}
