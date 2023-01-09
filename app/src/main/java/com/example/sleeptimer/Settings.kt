package com.example.sleeptimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val home = findViewById<ImageButton>(R.id.home)

        val backhome = Intent(this, MainActivity::class.java)

        var extendTime: Long = 10000 * 60

        val times = resources.getStringArray(R.array.Languages)

        var lastExtend = sharedPreferences!!.getInt("lastExtend", 1)

        val spinnerExtend = findViewById<Spinner>(R.id.spinnerExtend)

        val toggleMusic = findViewById<ToggleButton>(R.id.musicToggle)

        val toggleBlue = findViewById<ToggleButton>(R.id.blueToggle)

        val toggleWifi = findViewById<ToggleButton>(R.id.wifiToggle)

        toggleMusic.isChecked = sharedPreferences.getBoolean("toggleMusic", true)

        toggleBlue.isChecked = sharedPreferences.getBoolean("toggleBlue", true)

        toggleWifi.isChecked = sharedPreferences.getBoolean("toggleWifi", true)

        if (spinnerExtend != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
            spinnerExtend.adapter = adapter
            spinnerExtend.setSelection(lastExtend)

            spinnerExtend.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    when (position){
                        0 -> extendTime = 5000 * 60
                        1 -> extendTime = 10000 * 60
                        2 -> extendTime = 15000 * 60
                        3 -> extendTime = 20000 * 60
                        4 -> extendTime = 25000 * 60
                        5 -> extendTime = 30000 * 60
                    }
                    sharedEditor?.putLong("extend", extendTime)
                    sharedEditor?.putInt("lastExtend", position)
                    sharedEditor?.commit()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        home.setOnClickListener {
            startActivity(backhome)
        }

        toggleMusic.setOnClickListener {
            sharedEditor?.putBoolean("toggleMusic", toggleMusic.isChecked)
            sharedEditor?.commit()
        }

        toggleBlue.setOnClickListener {
            sharedEditor?.putBoolean("toggleBlue", toggleBlue.isChecked)
            sharedEditor?.commit()
        }

        toggleWifi.setOnClickListener {
            sharedEditor?.putBoolean("toggleWifi", toggleWifi.isChecked)
            sharedEditor?.commit()
        }
    }
}