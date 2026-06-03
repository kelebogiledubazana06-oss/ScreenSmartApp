package com.example.screensmart

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

fun Activity.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Activity.titleText(text: String, size: Float = 26f): TextView = TextView(this).apply {
    this.text = text
    textSize = size
    setTextColor(Color.rgb(88, 28, 135))
    typeface = Typeface.DEFAULT_BOLD
    gravity = Gravity.CENTER
}

fun Activity.bodyText(text: String, size: Float = 15f): TextView = TextView(this).apply {
    this.text = text
    textSize = size
    setTextColor(Color.rgb(67, 56, 202))
    gravity = Gravity.CENTER
    setLineSpacing(4f, 1.08f)
}

fun Activity.badgeText(text: String): TextView = TextView(this).apply {
    this.text = text
    textSize = 14f
    typeface = Typeface.DEFAULT_BOLD
    setTextColor(Color.WHITE)
    gravity = Gravity.CENTER
    setPadding(dp(12), dp(8), dp(12), dp(8))
    setBackgroundResource(R.drawable.badge_bg)
}

fun Activity.actionButton(text: String, primary: Boolean = true): Button = Button(this).apply {
    this.text = text
    textSize = 15f
    typeface = Typeface.DEFAULT_BOLD
    isAllCaps = false
    setTextColor(Color.WHITE)
    setBackgroundResource(if (primary) R.drawable.button_primary else R.drawable.button_secondary)
}

fun Activity.card(): LinearLayout = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    setBackgroundResource(R.drawable.card_bg)
    elevation = dp(5).toFloat()
    setPadding(dp(14), dp(14), dp(14), dp(14))
}

fun Activity.colourCard(background: Int): LinearLayout = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    setBackgroundResource(background)
    elevation = dp(5).toFloat()
    setPadding(dp(14), dp(12), dp(14), dp(12))
}

fun View.margin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val lp = LinearLayout.LayoutParams(layoutParams ?: LinearLayout.LayoutParams(-1, -2))
    lp.setMargins(left, top, right, bottom)
    layoutParams = lp
}
