package com.example.sleeptimer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val home = findViewById<ImageButton>(R.id.home)

        val backhome = Intent(this, MainActivity::class.java)

        var extendTime: Long = 10000 * 60

        val sharedPreferences = this?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

        val sharedEditor = sharedPreferences?.edit()

        val times = resources.getStringArray(R.array.Languages)

        var lastExtend = sharedPreferences!!.getInt("lastExtend", 1)

        val spinnerExtend = findViewById<Spinner>(R.id.spinnerExtend)

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
    }
}