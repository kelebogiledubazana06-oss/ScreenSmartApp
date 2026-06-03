
package com.example.screensmart

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import kotlin.system.exitProcess

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(18), dp(24), dp(18), dp(24))
            setBackgroundResource(R.drawable.app_gradient)
        }

        val logo = ImageView(this).apply {
            setImageResource(R.drawable.ic_screen_smart)
            adjustViewBounds = true
        }
        root.addView(logo, LinearLayout.LayoutParams(dp(140), dp(140)).apply { bottomMargin = dp(18) })

        root.addView(TextView(this).apply {
            text = "Screen Smart"
            textSize = 34f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        })

        root.addView(TextView(this).apply {
            text = "Weekly screen-time manager • ${ScreenTimeRepository.currentDateTime()}"
            textSize = 16f
            setTextColor(Color.rgb(224, 242, 254))
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(14) })

        val infoCard = colourCard(R.drawable.card_purple).apply {
            addView(bodyText("Rotondwa Sidney Netshidzivhani\nSparks_992", 16f))
        }
        root.addView(infoCard, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(14) })

        val start = actionButton("Enter App", true).apply {
            setOnClickListener { startActivity(Intent(this@SplashActivity, MainActivity::class.java)) }
        }
        val exit = actionButton("Exit", false).apply { setOnClickListener { finishAffinity(); exitProcess(0) } }
        root.addView(start, LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(12) })
        root.addView(exit, LinearLayout.LayoutParams(-1, dp(54)))

        setContentView(ScrollView(this).apply { addView(root) })
    }
}
