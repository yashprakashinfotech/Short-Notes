package com.yash.shortnotes.activity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import com.yash.shortnotes.R
import com.yash.shortnotes.helper.KeyClass

class AlarmActivity : AppCompatActivity() {

    private lateinit var txtAlertNoteTitle : TextView
    private lateinit var txtAlertNoteDescription : TextView
    private lateinit var txtAlertNoteDate : TextView
    private lateinit var txtAlertNoteTime : TextView
    private lateinit var btnStop : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        initView()
        val noteTitle = intent!!.getStringExtra(KeyClass.KEY_PENDING_TITLE1)
        val noteDescription = intent.getStringExtra(KeyClass.KEY_PENDING_DESCRIPTION1)
        val noteDate = intent.getStringExtra(KeyClass.KEY_PENDING_DATE1)
        val noteTime = intent.getStringExtra(KeyClass.KEY_PENDING_TIME1)

        txtAlertNoteTitle.text = noteTitle
        txtAlertNoteDescription.text = noteDescription
        txtAlertNoteDate.text = noteDate
        txtAlertNoteTime.text = noteTime

        val mp = MediaPlayer.create(this,Settings.System.DEFAULT_RINGTONE_URI)
        mp.start()

        btnStop.setOnClickListener {
            mp.stop()
        }
    }

    private fun initView(){
        txtAlertNoteTitle = findViewById(R.id.txtAlertNoteTitle)
        txtAlertNoteDescription = findViewById(R.id.txtAlertNoteDescription)
        txtAlertNoteDate = findViewById(R.id.txtAlertNoteDate)
        txtAlertNoteTime = findViewById(R.id.txtAlertNoteTime)
        btnStop = findViewById(R.id.btnStop)
    }
}