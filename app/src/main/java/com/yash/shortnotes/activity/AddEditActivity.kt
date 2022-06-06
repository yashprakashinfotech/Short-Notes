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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yash.shortnotes.R
import com.yash.shortnotes.helper.AlarmReceiver
import com.yash.shortnotes.helper.KeyClass
import com.yash.shortnotes.model.Note
import com.yash.shortnotes.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*


class AddEditActivity : AppCompatActivity() {

    private lateinit var etNoteTitle : EditText
    private lateinit var etNoteDescription : EditText
    private lateinit var etDate : EditText
    private lateinit var etTime : EditText
    private lateinit var btnAddUpdate : Button
    private lateinit var switchAlarm : SwitchMaterial

    private lateinit var noteViewModel : NoteViewModel
    private var noteId = -1

    private var noteAlertTime = ""
    private var dateSetMillis = ""
    private var timeSetMillis = ""

    private lateinit var iBroadCast : Intent
    private lateinit var pi : PendingIntent

    private lateinit var alarmManager : AlarmManager
    private var ALARM_REQUEST_CODE = 100

    @SuppressLint("SimpleDateFormat", "UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.hide()
        initView()

        // this is the Multiple alarm View
//        multipleAlarm()

        val noteType = intent.getStringExtra(KeyClass.KEY_NOTE_TYPE)
        val updateBtnText = "Update Note"
        val saveBtnText = "Save Note"

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

        btnAddUpdate.setOnClickListener {
            val noteTitle = etNoteTitle.text.toString()
            val noteDescription = etNoteDescription.text.toString()

            val datePick = etDate.text.toString()
            val timePick = etTime.text.toString()


            if (switchAlarm.isChecked){  // Check Switch is active

                if (noteType == "Edit"){ // Update the Note

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

//                        if (datePick.isNotEmpty() && timePick.isNotEmpty()){
//
//                            val myDate = "$etDate $etTime:00"
////                            val myDate = "2014/10/29 18:10:45"
//                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                            val date = sdf.parse(myDate)
//                            val millis = date?.time
//                            Log.d("boss","$millis")
//
//                            val alertTimeMillis = millis
//
//                            val iBroadCast = Intent(this,MyTimerReceiver::class.java)
//
//                            pi = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,iBroadCast,
//                                PendingIntent.FLAG_UPDATE_CURRENT)
//
//                            alarmManager.set(AlarmManager.RTC_WAKEUP,millis!!, pi)
//                            Toast.makeText(this,"Alarm Set!",Toast.LENGTH_SHORT).show()
//                        }else{
//                            Toast.makeText(this,"Please select Date And Time!",Toast.LENGTH_SHORT).show()
//                        }

                        // Set the Time For Alert
                        val myDate = "$datePick $timePick:00"

                        val sdfAlert = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date = sdfAlert.parse(myDate)
                        val millis = date?.time
                        Log.d("boss","$millis")
                        val alertTimeMillis = millis.toString()

                        // Set Alarm For Note
                        iBroadCast = Intent(this,AlarmReceiver::class.java)
                        val pi = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis!!,pi)

                        // for set date and time at note change
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        val updateNote = Note(noteTitle,noteDescription,currentDate,alertTimeMillis)
                        updateNote.id = noteId
                        noteViewModel.updateNote(updateNote)
                        Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
                    }

//                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() && datePick.isNotEmpty() && timePick.isNotEmpty()){
//                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
//                        val currentDate: String = sdf.format(Date())
//                        val updateNote = Note(noteTitle,noteDescription,currentDate)
//                        updateNote.id = noteId
//                        noteViewModel.updateNote(updateNote)
//                        Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
//                    }

                }else{ // Add the New Note

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
                    else{ // All validation is correct

                        // Set the Time For Alert
                        val myDate = "$datePick $timePick:00"
//                        val myDate = "2014/10/29 18:10:45"
                        val sdfAlert = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date = sdfAlert.parse(myDate)
                        val millis = date?.time
                        Log.d("boss","$millis")
                        val alertTimeMillis = millis.toString()

                        // Set Alarm For Note
                        iBroadCast = Intent(this,AlarmReceiver::class.java)
                        pi = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis!!,pi)

                        pi = PendingIntent.getBroadcast(this,1,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,millis+20000,pi)

                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate,alertTimeMillis))
                        Toast.makeText(this,"Note Added", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        this.finish()
                    }

//                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty() && datePick.isNotEmpty() && timePick.isNotEmpty()){
//                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
//                        val currentDate: String = sdf.format(Date())
//                        noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate))
//                        Toast.makeText(this,"Note Added", Toast.LENGTH_SHORT).show()
//
//                        startActivity(Intent(applicationContext, MainActivity::class.java))
//                        this.finish()
//                    }
//                    else{
//                        Toast.makeText(this,"Please Write Note!", Toast.LENGTH_SHORT).show()
//                    }
                }

            }else{

                val alertTime = ""
                if (noteType == "Edit"){ // Update the Note

                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        val updateNote = Note(noteTitle,noteDescription,currentDate,alertTime)
                        updateNote.id = noteId
                        noteViewModel.updateNote(updateNote)
                        Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
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

//            if (noteType == "Edit"){ // Update the Note
//
//                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
//                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
//                    val currentDate: String = sdf.format(Date())
//                    val updateNote = Note(noteTitle,noteDescription,currentDate)
//                    updateNote.id = noteId
//                    noteViewModel.updateNote(updateNote)
//                    Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
//                }
//
//            }else{ // Add the New Note
//                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
//                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
//                    val currentDate: String = sdf.format(Date())
//                    noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate))
//                    Toast.makeText(this,"Note Added", Toast.LENGTH_SHORT).show()
//
//                    startActivity(Intent(applicationContext, MainActivity::class.java))
//                    this.finish()
//                }else{
//                    Toast.makeText(this,"Please Write Note!", Toast.LENGTH_SHORT).show()
//                }
//            }

//            startActivity(Intent(applicationContext, MainActivity::class.java))
//            this.finish()
        }

        datePicker()

        etTime.setOnClickListener {
            mTimePicker()
        }

        switchAlarm.setOnCheckedChangeListener { button, isCheck ->
            if (isCheck){
                etTime.visibility = View.VISIBLE
                etDate.visibility = View.VISIBLE

            }else{
                etTime.visibility = View.GONE
                etDate.visibility = View.GONE
            }
        }
    }

    // try Multiple alarm
//    private fun multipleAlarm(){
//
//        val minutes = ArrayList<Long>()
//
//        minutes.add(1654498080000)
//        minutes.add(1654498140000)
//        minutes.add(1654498200000)
//        minutes.add(1654498260000)
//
//        val alarmManagers = arrayOfNulls<AlarmManager>(minutes.size)
//        val intents = arrayOfNulls<Intent>(alarmManagers.size)
//
//        for (i in alarmManagers.indices) {
//            intents[i] = Intent(applicationContext, AlarmReceiver::class.java)
//            /*
//        Here is very important,when we set one alarm, pending intent id becomes zero
//        but if we want set multiple alarms pending intent id has to be unique so i counter
//        is enough to be unique for PendingIntent
//      */
////            val pendingIntent: PendingIntent = PendingIntent.getBroadCast(applicationContext, i, intents[i], 0)
//            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, i, intents[i]!!, 0)
//
//            val calendar = Calendar.getInstance()
//            calendar[Calendar.MINUTE] = minutes[i].toInt()
//            alarmManagers[i] = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
////            alarmManagers[i]!![AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
//            alarmManagers[i]!!.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
////            alarmManagers[i]!![AlarmManager.RTC_WAKEUP, calendar.getTimeInMilis()] = pendingIntent
//        }
//
////        val alarmManager  = AlarmManager[minutes.size]()
//    }

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
//        // build the MaterialTimePicker Dialog
//        mTimePicker = MaterialTimePicker.Builder()
//            .setTimeFormat(clockFormat)
//            .setHour(8)
//            .setMinute(30)
//            .setTitleText("Set Time")
//            .build()

        // Show the MaterialTimePicker
        mTimePicker.show(supportFragmentManager,"Boss")

        // Action by Positive response "OK"
        mTimePicker.addOnPositiveButtonClickListener {
            val hour = mTimePicker.hour
            val minute = mTimePicker.minute
            etTime.setText(String.format("%02d:%02d",hour,minute))
        }

    }
}