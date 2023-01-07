package com.example.sleeptimer

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import com.example.sleeptimer.R.*

class MainActivity : AppCompatActivity() {

    var startTimeMS: Long = 10000 * 60

   var extendMS: Long = 10000 * 60

    var timeMS: Long = startTimeMS

    lateinit var startButton : Button

    lateinit var resetButton : Button

    lateinit var m10Button : Button

    lateinit var m5Button : Button

    lateinit var p5Button : Button

    lateinit var p10Button : Button

    lateinit var settings : ImageButton

    lateinit var extendButton : Button

    lateinit var timer : TextView

    lateinit var countDownTimer: CountDownTimer

    lateinit var sharedPreferences: SharedPreferences

    val manageBluetooth = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        sharedPreferences = this?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)!!

        val sharedEditor = sharedPreferences?.edit()

        startTimeMS = sharedPreferences?.getLong("startTime", 10000 * 60)!!

       extendMS = sharedPreferences.getLong("extend", 10000 * 60)

        timeMS = startTimeMS

        startButton = findViewById(id.start)

        resetButton = findViewById(id.reset)

        extendButton = findViewById(id.extend)

        m10Button = findViewById(id.buttonM10)

        m5Button = findViewById(id.buttonM5)

        p5Button = findViewById(id.buttonP5)

        p10Button = findViewById(id.buttonP10)

        timer = findViewById<EditText>(id.timer)

        settings = findViewById(id.settingButton)

        val goSetting = Intent(this, Settings::class.java)

        updateText()

        settings.setOnClickListener {
            startActivity(goSetting)
        }

        startButton.setOnClickListener {
            startTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
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
            countDownTimer.cancel()
            startTimer()
        }

    }

    private fun updateText() {
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

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeMS, 1000) {

            override fun onTick(p: Long) {
                timeMS = p
                updateText()
                startButton.visibility = View.INVISIBLE
                m10Button.visibility = View.INVISIBLE
                p10Button.visibility = View.INVISIBLE
                m5Button.visibility = View.INVISIBLE
                p5Button.visibility = View.INVISIBLE
                resetButton.visibility = View.VISIBLE
                extendButton.visibility = View.VISIBLE
            }

            override fun onFinish() {
                timer.text = "ENDED"
                if (sharedPreferences.getBoolean("toggleMusic", true)){

                }
                if (sharedPreferences.getBoolean("toggleBlue", true)){
                    manageBluetooth.disable()
                }
                if (sharedPreferences.getBoolean("toggleWifi", true)){

                }
            }
        }.start()
    }

fun resetTimer() {
        countDownTimer.cancel()
        timeMS = startTimeMS
        updateText()
        resetButton.visibility = View.INVISIBLE
        extendButton.visibility = View.INVISIBLE
        startButton.visibility = View.VISIBLE
        m10Button.visibility = View.VISIBLE
        p10Button.visibility = View.VISIBLE
        m5Button.visibility = View.VISIBLE
        p5Button.visibility = View.VISIBLE
    }
}