package com.example.sleeptimer

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.CountDownTimer
import android.os.IBinder

class CountdownService : Service() {

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
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null) {
            bluetoothAdapter.disable()
        }
    }

    private fun stopWifi(){
            val wifiManager =
                getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiManager.isWifiEnabled = false
        }
}
