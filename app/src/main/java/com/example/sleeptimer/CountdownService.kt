package com.example.sleeptimer

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.CountDownTimer
import android.os.IBinder
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

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
                stopSound()
                makeToast()
            }
        }
        countDownTimer?.start()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    fun makeToast(){
        Toast.makeText(
            this,
            "Have a good night !",
            Toast.LENGTH_LONG
        ).show()
    }

    fun stopSound() {
        val context = this
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val initialVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val fadeDuration = 5000
        val fadeSteps = 10

        val handler = Handler()

        val volumeStep = initialVolume.toFloat() / fadeSteps

        for (i in 1..fadeSteps) {
            handler.postDelayed({
                val newVolume = (initialVolume - volumeStep * i).toInt()

                val targetVolume = if (newVolume < 0) 0 else newVolume

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0)
            }, (fadeDuration / fadeSteps) * i.toLong())
        }

        handler.postDelayed({
            if (audioManager.isMusicActive) {
                audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
            }
        }, fadeDuration.toLong())
    }
}
