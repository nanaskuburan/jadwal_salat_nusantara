package com.example.calculator

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

object JavaneseCalendar {
    private val PASARAN_ID = listOf("Legi", "Pahing", "Pon", "Wage", "Kliwon")
    private val PASARAN_AR = listOf("ليغي", "باهينغ", "فون", "واغي", "كليوون")

    /**
     * Calculates Javanese Pasaran (Legi, Pahing, Pon, Wage, Kliwon)
     * Reference: 2024-01-01 was Monday Pahing (index 1)
     */
    fun getPasaran(year: Int, month: Int, day: Int): String {
        val cal = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(year, month - 1, day)
        }
        val ref = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(2024, 0, 1) // 2024-01-01 was Pahing (index 1)
        }
        val diffDays = ((cal.timeInMillis - ref.timeInMillis) / 86400000L).toInt()
        val index = Math.floorMod(1 + diffDays, 5)
        return PASARAN_ID[index]
    }

    fun getPasaranAr(year: Int, month: Int, day: Int): String {
        val cal = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(year, month - 1, day)
        }
        val ref = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(2024, 0, 1)
        }
        val diffDays = ((cal.timeInMillis - ref.timeInMillis) / 86400000L).toInt()
        val index = Math.floorMod(1 + diffDays, 5)
        return PASARAN_AR[index]
    }
}
