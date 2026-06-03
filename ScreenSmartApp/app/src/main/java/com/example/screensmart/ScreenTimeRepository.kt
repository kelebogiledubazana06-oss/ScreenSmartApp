package com.example.screensmart

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object ScreenTimeRepository {
    const val DAYS_IN_WEEK = 7
    const val HEALTHY_LIMIT_MINUTES = 120

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy • HH:mm")

    // Real dates are generated from the device date instead of fixed example dates.
    val days = Array(DAYS_IN_WEEK) { index ->
        LocalDate.now().minusDays((DAYS_IN_WEEK - 1 - index).toLong()).format(dateFormatter)
    }

    val morningMinutes = IntArray(DAYS_IN_WEEK) { 0 }
    val afternoonMinutes = IntArray(DAYS_IN_WEEK) { 0 }
    val activityNotes = Array(DAYS_IN_WEEK) { "" }
    val capturedAt = Array(DAYS_IN_WEEK) { "Not captured yet" }

    fun currentDateTime(): String = LocalDateTime.now().format(dateTimeFormatter)

    fun saveDay(index: Int, morning: Int, afternoon: Int, note: String) {
        morningMinutes[index] = morning
        afternoonMinutes[index] = afternoon
        activityNotes[index] = note.trim()
        capturedAt[index] = currentDateTime()
    }

    fun clearAll() {
        for (i in 0 until DAYS_IN_WEEK) {
            morningMinutes[i] = 0
            afternoonMinutes[i] = 0
            activityNotes[i] = ""
            capturedAt[i] = "Not captured yet"
        }
    }

    fun loadSampleData() {
        val sampleMorning = intArrayOf(35, 25, 80, 55, 95, 45, 20)
        val sampleAfternoon = intArrayOf(50, 40, 100, 65, 120, 75, 30)
        val sampleNotes = arrayOf(
            "Used devices for class notes, LMS access and research.",
            "Watched educational videos and replied to group messages.",
            "Completed online tutorials and prepared a presentation.",
            "Used phone for study timer, reminders and quick research.",
            "Long entertainment session after completing assignments.",
            "Balanced social media with revision videos and reading.",
            "Light usage because most tasks were completed offline."
        )
        for (i in 0 until DAYS_IN_WEEK) saveDay(i, sampleMorning[i], sampleAfternoon[i], sampleNotes[i])
    }

    fun totalForDay(index: Int): Int = morningMinutes[index] + afternoonMinutes[index]

    fun weeklyTotal(): Int {
        var total = 0
        for (i in 0 until DAYS_IN_WEEK) total += totalForDay(i)
        return total
    }

    fun averagePerDay(): Double = weeklyTotal().toDouble() / DAYS_IN_WEEK

    fun highestDayIndex(): Int {
        var highestIndex = 0
        for (i in 1 until DAYS_IN_WEEK) if (totalForDay(i) > totalForDay(highestIndex)) highestIndex = i
        return highestIndex
    }

    fun lowestDayIndex(): Int {
        var lowestIndex = 0
        for (i in 1 until DAYS_IN_WEEK) if (totalForDay(i) < totalForDay(lowestIndex)) lowestIndex = i
        return lowestIndex
    }

    fun healthyDayCount(): Int {
        var count = 0
        for (i in 0 until DAYS_IN_WEEK) if (totalForDay(i) <= HEALTHY_LIMIT_MINUTES) count++
        return count
    }

    fun statusFor(total: Int): String = when {
        total == 0 -> "No screen-time recorded"
        total <= HEALTHY_LIMIT_MINUTES -> "Healthy balance"
        total <= 180 -> "Moderate usage"
        else -> "High usage - reduce screen time"
    }
}
