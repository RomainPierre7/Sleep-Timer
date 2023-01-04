package com.example.sleeptimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val home = findViewById<ImageButton>(R.id.home)

        val backhome = Intent(this, MainActivity::class.java)

        var extendTime = 10

        val extendIntent = Intent(this, MainActivity::class.java).apply{
            putExtra("EXTRA_EXTEND", extendTime)
        }

        val times = resources.getStringArray(R.array.Languages)

        val spinnerExtend = findViewById<Spinner>(R.id.spinnerExtend)

        if (spinnerExtend != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
            spinnerExtend.adapter = adapter

            spinnerExtend.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    when (position){
                        0 -> extendTime = 5
                        1 -> extendTime = 10
                        2 -> extendTime = 15
                        3 -> extendTime = 20
                        4 -> extendTime = 25
                        5 -> extendTime = 30
                    }
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