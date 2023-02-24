package com.example.sleeptimer

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions

class CountdownService : Service() {

    private val REQUEST_CODE_BLUETOOTH = 1

    private var countDownTimer: CountDownTimer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        countDownTimer = object : CountDownTimer(timeMS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeMS = millisUntilFinished
                updateText()
            }

            override fun onFinish() {
                if (sharedPreferences?.getBoolean("toggleMusic", true) == true){
                    stopMusic()
                }
                if (sharedPreferences?.getBoolean("toggleBlue", true) == true){
                    stopBlue()
                }
                if (sharedPreferences?.getBoolean("toggleWifi", true) == true){
                    stopWifi()
                }
            }
        }
        countDownTimer?.start()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    private fun stopMusic(){
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.isVolumeFixed) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT)
        }

    }

    private fun stopBlue(){
        val REQUEST_CODE_BLUETOOTH = 1


    }

    private fun stopWifi(){
            val wifiManager =
                getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiManager.isWifiEnabled = false
        }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_BLUETOOTH -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted
                    // You can call a function that requires the permission here
                } else {
                    Toast.makeText(this, "Bluetooth permission was not granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Handle other permissions here
            }
        }
    }

    fun requestBluetoothPermission() {
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_CODE_BLUETOOTH)
        } else {
            // Permission has already been granted
            // You can call a function that requires the permission here
        }
    }
}
