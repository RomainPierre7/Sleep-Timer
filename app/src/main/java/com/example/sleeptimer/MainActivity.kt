package com.example.sleeptimer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sleeptimer.R.id
import com.example.sleeptimer.R.layout

var firstLaunch = true

var startTimeMS: Long = 10000 * 60

var extendMS: Long = 10000 * 60

var timeMS: Long = startTimeMS

lateinit var timer : TextView

lateinit var sharedPreferences: SharedPreferences

lateinit var sharedEditor: SharedPreferences.Editor

class MainActivity : AppCompatActivity() {

    lateinit var startButton : Button

    lateinit var stopButton : Button

    lateinit var m10Button : Button

    lateinit var m5Button : Button

    lateinit var p5Button : Button

    lateinit var p10Button : Button

    lateinit var settings : ImageButton

    lateinit var extendButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        fun BatteryOptimization(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val packageName = context.packageName
                val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    intent.data = Uri.parse("package:$packageName")
                    context.startActivity(intent)
                }
            }
        }

        fun firstLaunchDialog(context: Context) {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setTitle("Welcome on Sleep Timer")
            builder.setMessage("To ensure correct operation of the application, please disable battery optimization for this application.")

            builder.setPositiveButton("Go to settings") { dialog, which ->
                BatteryOptimization(context)
            }
            builder.show()
        }

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        sharedEditor = sharedPreferences?.edit()!!

        firstLaunch = sharedPreferences.getBoolean("firstLaunch", true)
        if(firstLaunch == true){
            firstLaunchDialog(this)
            sharedEditor?.putBoolean("firstLaunch", false)
            sharedEditor?.commit()
        }

        startTimeMS = sharedPreferences?.getLong("startTime", 10000 * 60)!!

       extendMS = sharedPreferences.getLong("extend", 10000 * 60)

        timeMS = startTimeMS

        startButton = findViewById(id.start)

        stopButton = findViewById(id.stop)

        extendButton = findViewById(id.extend)

        m10Button = findViewById(id.buttonM10)

        m5Button = findViewById(id.buttonM5)

        p5Button = findViewById(id.buttonP5)

        p10Button = findViewById(id.buttonP10)

        timer = findViewById<EditText>(id.timer)

        settings = findViewById(id.settingButton)

        val goSetting = Intent(this, SettingsActivity::class.java)

        updateText()

        settings.setOnClickListener {
            startActivity(goSetting)
        }

        settings.setOnLongClickListener {
            timeMS = 3000
            startTimeMS = timeMS
            sharedEditor?.putLong("startTime", startTimeMS)
            sharedEditor?.commit()
            updateText()
            Toast.makeText(
                this,
                "Debug easter egg",
                Toast.LENGTH_LONG
            ).show()
            true
        }

        startButton.setOnClickListener {
            startTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }

        m10Button.setOnClickListener {
            timeMS -= 10000 * 60
            if (timeMS < 0){
                timeMS = 0
            }
            startTimeMS = timeMS
            sharedEditor?.putLong("startTime", startTimeMS)
            sharedEditor?.commit()
            updateText()
        }

        m5Button.setOnClickListener {
            timeMS -= 5000 * 60
            if (timeMS < 0){
                timeMS = 0
            }
            startTimeMS = timeMS
            sharedEditor?.putLong("startTime", startTimeMS)
            sharedEditor?.commit()
            updateText()
        }

        p5Button.setOnClickListener {
            timeMS += 5000 * 60
            if (timeMS > 10 * 1000 * 3600){
                timeMS = 10 * 1000 * 3600
            }
            startTimeMS = timeMS
            sharedEditor?.putLong("startTime", startTimeMS)
            sharedEditor?.commit()
            updateText()
        }

        p10Button.setOnClickListener {
            timeMS += 10000 * 60
            if (timeMS > 10 * 1000 * 3600){
                timeMS = 10 * 1000 * 3600
            }
            startTimeMS = timeMS
            sharedEditor?.putLong("startTime", startTimeMS)
            sharedEditor?.commit()
            updateText()
        }

        extendButton.setOnClickListener {
            if (timeMS + extendMS > 10 * 1000 * 3600){
                timeMS = 10 * 1000 * 3600
            } else {
                timeMS += extendMS
            }
            val intent = Intent(this, CountdownService::class.java)
            stopService(intent)
            startTimer()
        }
    }

    private fun startTimer() {
        val intent = Intent(this, CountdownService::class.java)
        startService(intent)
        stopButton.visibility = View.VISIBLE
        extendButton.visibility = View.VISIBLE
        startButton.visibility = View.INVISIBLE
        m10Button.visibility = View.INVISIBLE
        p10Button.visibility = View.INVISIBLE
        m5Button.visibility = View.INVISIBLE
        p5Button.visibility = View.INVISIBLE
    }

fun stopTimer() {
        val intent = Intent(this, CountdownService::class.java)
        stopService(intent)
        timeMS = startTimeMS
        updateText()
        stopButton.visibility = View.INVISIBLE
        extendButton.visibility = View.INVISIBLE
        startButton.visibility = View.VISIBLE
        m10Button.visibility = View.VISIBLE
        p10Button.visibility = View.VISIBLE
        m5Button.visibility = View.VISIBLE
        p5Button.visibility = View.VISIBLE
    }
}

fun updateText() {
    val heures = (timeMS / 1000) / 3600
    val minutes = ((timeMS / 1000) - (heures * 3600)) / 60
    val seconds = ((timeMS / 1000) - (heures * 3600) - (minutes * 60))% 60

    if (heures < 1){
        if ((minutes < 10) && (seconds < 10)){
            timer.text = "0$minutes:0$seconds"
        }
        else if ((minutes < 10) && (seconds >= 10)){
            timer.text = "0$minutes:$seconds"
        }
        else if ((minutes >= 10) && (seconds < 10)){
            timer.text = "$minutes:0$seconds"
        }
        else{
            timer.text = "$minutes:$seconds"
        }
    } else if (heures < 10){
        if ((minutes < 10) && (seconds < 10)){
            timer.text = "0$heures:0$minutes:0$seconds"
        }
        else if ((minutes < 10) && (seconds >= 10)){
            timer.text = "0$heures:0$minutes:$seconds"
        }
        else if ((minutes >= 10) && (seconds < 10)){
            timer.text = "0$heures:$minutes:0$seconds"
        }
        else{
            timer.text = "0$heures:$minutes:$seconds"
        }
    } else {
        if ((minutes < 10) && (seconds < 10)){
            timer.text = "$heures:0$minutes:0$seconds"
        }
        else if ((minutes < 10) && (seconds >= 10)){
            timer.text = "$heures:0$minutes:$seconds"
        }
        else if ((minutes >= 10) && (seconds < 10)){
            timer.text = "$heures:$minutes:0$seconds"
        }
        else{
            timer.text = "$heures:$minutes:$seconds"
        }
    }
}