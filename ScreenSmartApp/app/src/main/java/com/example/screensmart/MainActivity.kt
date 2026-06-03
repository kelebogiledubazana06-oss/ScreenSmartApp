
package com.example.screensmart

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val morningInputs = ArrayList<EditText>()
    private val afternoonInputs = ArrayList<EditText>()
    private val noteInputs = ArrayList<EditText>()
    private lateinit var statsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildScreen()
    }

    private fun buildScreen() {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(16), dp(14), dp(16))
            setBackgroundResource(R.drawable.app_gradient)
        }

        container.addView(titleText("Screen Smart Dashboard", 28f).apply { setTextColor(Color.WHITE) })
        container.addView(badgeText("Today: ${ScreenTimeRepository.currentDateTime()}").apply { margin(top = dp(8), bottom = dp(10)) })
        container.addView(bodyText("Capture morning and afternoon screen time using real dates from your device. Your weekly balance updates instantly.", 15f).apply { setTextColor(Color.WHITE); margin(bottom = dp(10)) })

        statsText = bodyText("", 15f).apply {
            gravity = Gravity.START
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(15, 23, 42))
        }
        container.addView(colourCard(R.drawable.card_green).apply { addView(statsText) }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(8) })

        for (i in 0 until ScreenTimeRepository.DAYS_IN_WEEK) {
            val dayCard = colourCard(when (i % 4) { 0 -> R.drawable.card_pink; 1 -> R.drawable.card_blue; 2 -> R.drawable.card_green; else -> R.drawable.card_purple })
            dayCard.addView(TextView(this).apply {
                text = "Day ${i + 1}: ${ScreenTimeRepository.days[i]}"
                textSize = 18f
                setTextColor(Color.rgb(124, 58, 237))
                typeface = Typeface.DEFAULT_BOLD
            })

            val morning = createNumberInput("Morning minutes")
            val afternoon = createNumberInput("Afternoon minutes")
            val note = createTextInput("Activity notes")

            morning.setText(if (ScreenTimeRepository.morningMinutes[i] == 0) "" else ScreenTimeRepository.morningMinutes[i].toString())
            afternoon.setText(if (ScreenTimeRepository.afternoonMinutes[i] == 0) "" else ScreenTimeRepository.afternoonMinutes[i].toString())
            note.setText(ScreenTimeRepository.activityNotes[i])

            morningInputs.add(morning)
            afternoonInputs.add(afternoon)
            noteInputs.add(note)

            dayCard.addView(morning)
            dayCard.addView(afternoon)
            dayCard.addView(note)
            container.addView(dayCard, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(8) })
        }

        container.addView(actionButton("Save and Analyse Week", true).apply { setOnClickListener { saveInputs() } }, LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(8) })
        container.addView(actionButton("Open Detailed View", true).apply { setOnClickListener { startActivity(Intent(this@MainActivity, DetailedViewActivity::class.java)) } }, LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(8) })
        container.addView(actionButton("Load Sample Data", false).apply { setOnClickListener { loadSamplesIntoScreen() } }, LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(8) })
        container.addView(actionButton("Clear All Data", false).apply { setOnClickListener { clearInputs() } }, LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(8) })
        container.addView(actionButton("Back to Splash Screen", false).apply { setOnClickListener { finish() } }, LinearLayout.LayoutParams(-1, dp(54)))

        refreshStats()
        setContentView(ScrollView(this).apply { setBackgroundResource(R.drawable.app_gradient); addView(container) })
    }

    private fun createNumberInput(hintText: String): EditText = EditText(this).apply {
        hint = hintText
        inputType = InputType.TYPE_CLASS_NUMBER
        setSingleLine(true)
        textSize = 15f
        setPadding(dp(12), dp(8), dp(12), dp(8))
    }

    private fun createTextInput(hintText: String): EditText = EditText(this).apply {
        hint = hintText
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        minLines = 1
        maxLines = 3
        textSize = 15f
        setPadding(dp(12), dp(8), dp(12), dp(8))
    }

    private fun saveInputs() {
        for (i in 0 until ScreenTimeRepository.DAYS_IN_WEEK) {
            val morningText = morningInputs[i].text.toString().trim()
            val afternoonText = afternoonInputs[i].text.toString().trim()
            val noteText = noteInputs[i].text.toString().trim()

            if (morningText.isEmpty() || afternoonText.isEmpty() || noteText.isEmpty()) {
                showError("Please complete all fields for ${ScreenTimeRepository.days[i]}.")
                return
            }

            val morning = morningText.toIntOrNull()
            val afternoon = afternoonText.toIntOrNull()
            if (morning == null || afternoon == null) {
                showError("Screen-time values must be valid whole numbers.")
                return
            }
            if (morning < 0 || afternoon < 0) {
                showError("Screen-time values cannot be negative.")
                return
            }
            if (morning > 720 || afternoon > 720) {
                showError("One session cannot exceed 720 minutes. Please enter a realistic value.")
                return
            }
            if (noteText.length < 5) {
                showError("Please provide a clearer activity note for ${ScreenTimeRepository.days[i]}.")
                return
            }
            ScreenTimeRepository.saveDay(i, morning, afternoon, noteText)
        }
        refreshStats()
        Toast.makeText(this, "Weekly screen-time data saved successfully.", Toast.LENGTH_LONG).show()
    }

    private fun loadSamplesIntoScreen() {
        ScreenTimeRepository.loadSampleData()
        for (i in 0 until ScreenTimeRepository.DAYS_IN_WEEK) {
            morningInputs[i].setText(ScreenTimeRepository.morningMinutes[i].toString())
            afternoonInputs[i].setText(ScreenTimeRepository.afternoonMinutes[i].toString())
            noteInputs[i].setText(ScreenTimeRepository.activityNotes[i])
        }
        refreshStats()
        Toast.makeText(this, "Sample data loaded.", Toast.LENGTH_SHORT).show()
    }

    private fun clearInputs() {
        ScreenTimeRepository.clearAll()
        for (i in 0 until ScreenTimeRepository.DAYS_IN_WEEK) {
            morningInputs[i].text.clear()
            afternoonInputs[i].text.clear()
            noteInputs[i].text.clear()
        }
        refreshStats()
        Toast.makeText(this, "Data cleared. You can re-enter new values.", Toast.LENGTH_SHORT).show()
    }

    private fun refreshStats() {
        val highest = ScreenTimeRepository.highestDayIndex()
        val lowest = ScreenTimeRepository.lowestDayIndex()
        statsText.text = "Weekly Total: ${ScreenTimeRepository.weeklyTotal()} minutes\n" +
                "Average per Day: %.1f minutes\n".format(ScreenTimeRepository.averagePerDay()) +
                "Highest Usage: ${ScreenTimeRepository.days[highest]} (${ScreenTimeRepository.totalForDay(highest)} min)\n" +
                "Lowest Usage: ${ScreenTimeRepository.days[lowest]} (${ScreenTimeRepository.totalForDay(lowest)} min)\n" +
                "Healthy Days: ${ScreenTimeRepository.healthyDayCount()} / ${ScreenTimeRepository.DAYS_IN_WEEK}"
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
