package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worship_records")
data class WorshipRecord(
    @PrimaryKey val date: String, // YYYY-MM-DD
    val subuh: Boolean = false,
    val dzuhur: Boolean = false,
    val ashar: Boolean = false,
    val maghrib: Boolean = false,
    val isya: Boolean = false,
    val dhuha: Boolean = false,
    val quranPages: Int = 0,
    val notes: String = ""
)
