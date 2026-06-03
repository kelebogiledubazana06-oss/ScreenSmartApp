
package com.example.screensmart

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class DetailedViewActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildDetailedScreen()
    }

    private fun buildDetailedScreen() {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(16), dp(14), dp(16))
            setBackgroundResource(R.drawable.app_gradient)
        }

        container.addView(titleText("Detailed Weekly Report", 28f).apply { setTextColor(Color.WHITE) })
        container.addView(badgeText("Report generated: ${ScreenTimeRepository.currentDateTime()}").apply { margin(top = dp(8), bottom = dp(10)) })
        container.addView(bodyText("A colourful breakdown of daily usage, total minutes, activity notes, captured time and usage status.", 15f).apply { setTextColor(Color.WHITE); margin(bottom = dp(10)) })

        val highestIndex = ScreenTimeRepository.highestDayIndex()
        val lowestIndex = ScreenTimeRepository.lowestDayIndex()
        val summary = colourCard(R.drawable.card_green).apply {
            addView(TextView(this@DetailedViewActivity).apply {
                text = "Weekly Intelligence"
                textSize = 19f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.rgb(37, 99, 235))
            })
            addView(TextView(this@DetailedViewActivity).apply {
                text = "Total: ${ScreenTimeRepository.weeklyTotal()} minutes\n" +
                        "Average: %.1f minutes per day\n".format(ScreenTimeRepository.averagePerDay()) +
                        "Most Screen Time: ${ScreenTimeRepository.days[highestIndex]} (${ScreenTimeRepository.totalForDay(highestIndex)} min)\n" +
                        "Least Screen Time: ${ScreenTimeRepository.days[lowestIndex]} (${ScreenTimeRepository.totalForDay(lowestIndex)} min)\n" +
                        "Healthy Balance Days: ${ScreenTimeRepository.healthyDayCount()} / ${ScreenTimeRepository.DAYS_IN_WEEK}"
                textSize = 15f
                setTextColor(Color.rgb(51, 65, 85))
                setLineSpacing(5f, 1.1f)
            })
        }
        container.addView(summary, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10) })

        for (i in 0 until ScreenTimeRepository.DAYS_IN_WEEK) {
            val total = ScreenTimeRepository.totalForDay(i)
            val dayCard = colourCard(when (i % 4) { 0 -> R.drawable.card_pink; 1 -> R.drawable.card_blue; 2 -> R.drawable.card_green; else -> R.drawable.card_purple })
            dayCard.addView(TextView(this).apply {
                text = "${ScreenTimeRepository.days[i]}"
                textSize = 18f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.rgb(16, 24, 40))
            })
            dayCard.addView(TextView(this).apply {
                text = "Morning: ${ScreenTimeRepository.morningMinutes[i]} min\n" +
                        "Afternoon: ${ScreenTimeRepository.afternoonMinutes[i]} min\n" +
                        "Daily Total: $total min\n" +
                        "Status: ${ScreenTimeRepository.statusFor(total)}\n" +
                        "Captured: ${ScreenTimeRepository.capturedAt[i]}\n" +
                        "Notes: ${ScreenTimeRepository.activityNotes[i].ifBlank { "No notes captured" }}"
                textSize = 15f
                setTextColor(Color.rgb(71, 85, 105))
                setLineSpacing(5f, 1.1f)
            })
            container.addView(dayCard, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(8) })
        }

        container.addView(actionButton("Back to Main Screen", true).apply {
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(-1, dp(54)))

        setContentView(ScrollView(this).apply { setBackgroundResource(R.drawable.app_gradient); addView(container) })
    }
}
