package com.yash.shortnotes.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.color.MaterialColors
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.yash.shortnotes.R
import com.yash.shortnotes.helper.AlarmReceiver
import com.yash.shortnotes.helper.KeyClass
import com.yash.shortnotes.model.Note
import com.yash.shortnotes.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddEditActivity : AppCompatActivity() {

    private lateinit var etNoteTitle : EditText
    private lateinit var etNoteDescription : EditText
    private lateinit var etDate : EditText
    private lateinit var etTime : EditText
    private lateinit var btnAddUpdate : Button
    private lateinit var switchAlarm : SwitchMaterial

    private lateinit var noteViewModel : NoteViewModel
    private var noteId = -1

//    var maxId = 0

    private var noteAlertTime = ""
    private var dateSetMillis = ""
    private var timeSetMillis = ""

    private lateinit var iBroadCast : Intent
    private lateinit var pi : PendingIntent

    private lateinit var millisArray: ArrayList<Long>

    private lateinit var alarmManager : AlarmManager

    private var alarmRequestCode = 1000

    @SuppressLint("SimpleDateFormat", "UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setContentView(R.layout.activity_add_edit)

        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = buildTransition()
        window.sharedElementEnterTransition = buildTransition()
        window.sharedElementReenterTransition = buildTransition()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initView()

        // datePicker
        datePicker()
        etTime.setOnClickListener {
            mTimePicker() // TimePicker
        }

        // Alarm Switch
        switchAlarm.setOnCheckedChangeListener { button, isCheck ->
            if (isCheck){
                etTime.visibility = View.VISIBLE
                etDate.visibility = View.VISIBLE

            }else{
                etTime.visibility = View.GONE
                etDate.visibility = View.GONE
            }
        }

        // note Type from main Activity
        val noteType = intent.getStringExtra(KeyClass.KEY_NOTE_TYPE)
        val updateBtnText = "Update Note"
        val saveBtnText = "Save Note"
        // NoteType Check Edit or New Add
        if (noteType == "Edit"){
            val noteTitle = intent.getStringExtra(KeyClass.KEY_NOTE_TITLE)
            val noteDescription = intent.getStringExtra(KeyClass.KEY_NOTE_DESCRIPTION)
            noteAlertTime = intent.getStringExtra(KeyClass.KEY_NOTE_ALERT_TIME)!!
            noteId = intent.getIntExtra(KeyClass.KEY_NOTE_ID,-1)

            btnAddUpdate.text = updateBtnText
            etNoteTitle.setText(noteTitle)
            etNoteDescription.setText(noteDescription)

        }
        else{
            btnAddUpdate.text = saveBtnText
        }

        // Check Date Is available from Database in notes
        if (noteAlertTime.isNotEmpty()){
            switchAlarm.isChecked = true
            etTime.visibility = View.VISIBLE
            etDate.visibility = View.VISIBLE

            val millis = noteAlertTime.toLong()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val millisDate = formatter.format(Date(millis))

            dateSetMillis = millisDate.substring(0,10)
            timeSetMillis = millisDate.substring(11,16)

            etTime.setText(timeSetMillis)
            etDate.setText(dateSetMillis)
        }

        val maxId = noteViewModel.getMaxId().toString()
//        val id = maxId.toString()
//        val idMax = id.toInt()
        Log.d("boss","Hello $maxId")

        btnAddUpdate.setOnClickListener {
            Log.d("bosses","Hello button $maxId")
            val noteTitle = etNoteTitle.text.toString()
            val noteDescription = etNoteDescription.text.toString()

            val datePick = etDate.text.toString()
            val timePick = etTime.text.toString()

            if (switchAlarm.isChecked){  // Check Switch is active

                if (noteType == "Edit"){ // Update the Note

                    if (validationWithAlarm()){ // Check Field validation

                        // Set the Time For Alert
                        val myDate = "$datePick $timePick:00"

                        val sdfAlert = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date = sdfAlert.parse(myDate)
                        val millis = date?.time
                        Log.d("boss","$millis")
                        val alertTimeMillis = millis.toString()

                        // for set date and time at note change
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        val updateNote = Note(noteTitle,noteDescription,currentDate,alertTimeMillis)
                        updateNote.id = noteId
                        noteViewModel.updateNote(updateNote)

                        // Set Alarm For Note
                        iBroadCast = Intent(this,AlarmReceiver::class.java)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_TITLE,noteTitle)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_DESCRIPTION,noteDescription)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_DATE,datePick)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_TIME,timePick)
                        val pi = PendingIntent.getBroadcast(this, updateNote.id!!,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis!!,pi)

                        Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        this.finish()
                    }

                }else{ // Add the New Note

                    if (validationWithAlarm()){ // Check Field Validation
                        // Set the Time For Alert
                        val myDate = "$datePick $timePick:00"
//                        val myDate = "2014/10/29 18:10:45"
                        val sdfAlert = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date = sdfAlert.parse(myDate)
                        val millis = date?.time
                        Log.d("boss","$millis")
                        val alertTimeMillis = millis.toString()

                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate,alertTimeMillis))

//                        val maxId = noteViewModel.getMaxId()
//                        Log.d("boss","$maxId")

//                        val dataValue = noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate,alertTimeMillis).id)
//                        val dataValue = Note(noteTitle,noteDescription,currentDate,alertTimeMillis).id

                        // Set Alarm For Note
                        iBroadCast = Intent(this,AlarmReceiver::class.java)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_TITLE,noteTitle)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_DESCRIPTION,noteDescription)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_DATE,datePick)
                        iBroadCast.putExtra(KeyClass.KEY_PENDING_TIME,timePick)
                        pi = PendingIntent.getBroadcast(this,alarmRequestCode,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis!!,pi)

                        Toast.makeText(this,"Note Added", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        this.finish()
                    }
                }

            }else{ // When switch is not active.

                val alertTime = ""
                if (noteType == "Edit"){ // Update the Note

                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        val updateNote = Note(noteTitle,noteDescription,currentDate,alertTime)
                        updateNote.id = noteId
                        noteViewModel.updateNote(updateNote)
                        Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
                        this.finish()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }

                }else{ // Add the New Note
                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate,alertTime))
                        Toast.makeText(this,"Note Added", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        this.finish()
                    }else{
                        Toast.makeText(this,"Please Write Note!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initView() {

        etNoteTitle = findViewById(R.id.etNoteTitle)
        etNoteDescription = findViewById(R.id.etNoteDescription)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        switchAlarm = findViewById(R.id.switchAlarm)
        btnAddUpdate = findViewById(R.id.btnAddUpdate)
        noteViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NoteViewModel::class.java]

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        millisArray = ArrayList()
    }

    // For Date Picker
    private var year = 0
    private var month = 0
    private var day = 0
    private fun datePicker() {
        val cal = Calendar.getInstance()
        val cal1 = Calendar.getInstance()

        year = cal1.get(Calendar.YEAR)
        month = cal1.get(Calendar.MONTH)
        day = cal1.get(Calendar.DAY_OF_MONTH)

        // For Creating Date Picker Dialog Using Dynamic Code
        val dateShow =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                val myFormat = "yyyy-MM-dd"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                etDate.setText(dateFormat.format(cal.time))    // Set Date in EditText
            }

        // Date Piking When Click that EditText
        etDate.setOnClickListener {
            val dateDialog = DatePickerDialog(
                this, dateShow,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            //for min date set (At current Time & Date)
            dateDialog.datePicker.minDate = cal1.timeInMillis
            dateDialog.show()
        }
    }


    // Time Picker

    private fun mTimePicker(){

        val mTimePicker : MaterialTimePicker

        // Check System Format
        val isSystem24Hour = DateFormat.is24HourFormat(this)
        // timePicker Format according to System Format
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        if (timeSetMillis.isNotEmpty()){

            val h = timeSetMillis.substring(0,2)
            val hour = h.toInt()
            val m = timeSetMillis.substring(3)
            val minutes = m.toInt()

            // build the MaterialTimePicker Dialog
            mTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(hour)
                .setMinute(minutes)
                .setTitleText("Set Time")
                .build()
        }
        else{
            // build the MaterialTimePicker Dialog
            mTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set Time")
                .build()
        }

        // Show the MaterialTimePicker
        mTimePicker.show(supportFragmentManager,"Boss")

        // Action by Positive response "OK"
        mTimePicker.addOnPositiveButtonClickListener {
            val hour = mTimePicker.hour
            val minute = mTimePicker.minute
            etTime.setText(String.format("%02d:%02d",hour,minute))
        }

    }

    private fun validationWithAlarm() : Boolean{
        val noteTitle = etNoteTitle.text.toString()
        val noteDescription = etNoteDescription.text.toString()

        val datePick = etDate.text.toString()
        val timePick = etTime.text.toString()

        if (noteTitle.isEmpty()){
            Toast.makeText(this,"Note Title is Empty", Toast.LENGTH_SHORT).show()
        }
        else if (noteDescription.isEmpty()){
            Toast.makeText(this,"Note Description is Empty", Toast.LENGTH_SHORT).show()
        }
        else if (datePick.isEmpty()){
            Toast.makeText(this,"Note Date is not Pick", Toast.LENGTH_SHORT).show()
        }
        else if (timePick.isEmpty()){
            Toast.makeText(this,"Note Time is not Pick", Toast.LENGTH_SHORT).show()
        }
        else{
            return true
        }
        return false

    }

    private fun buildTransition(): com.google.android.material.transition.platform.MaterialContainerTransform {

        return com.google.android.material.transition.platform.MaterialContainerTransform().apply {
            addTarget(R.id.addEdit)
            setAllContainerColors(MaterialColors.getColor(findViewById(R.id.addEdit),
                com.google.android.material.R.attr.colorSurface))
            duration = 400
            pathMotion = com.google.android.material.transition.platform.MaterialArcMotion()
            interpolator = FastOutSlowInInterpolator()
            fadeMode = com.google.android.material.transition.platform.MaterialContainerTransform.FADE_MODE_IN
        }
    }
}