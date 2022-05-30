package com.yash.shortnotes.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yash.shortnotes.R
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

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.hide()
        initView()

        val noteType = intent.getStringExtra(KeyClass.KEY_NOTE_TYPE)
        val updateBtnText = "Update Note"
        val saveBtnText = "Save Note"

        if (noteType == "Edit"){

            val noteTitle = intent.getStringExtra(KeyClass.KEY_NOTE_TITLE)
            val noteDescription = intent.getStringExtra(KeyClass.KEY_NOTE_DESCRIPTION)
            noteId = intent.getIntExtra(KeyClass.KEY_NOTE_ID,-1)

            btnAddUpdate.text = updateBtnText
            etNoteTitle.setText(noteTitle)
            etNoteDescription.setText(noteDescription)

        }
        else{
            btnAddUpdate.text = saveBtnText
        }

        btnAddUpdate.setOnClickListener {
            val noteTitle = etNoteTitle.text.toString()
            val noteDescription = etNoteDescription.text.toString()

            val datePick = etDate.text.toString()
            val timePick = etTime.text.toString()

            if (switchAlarm.isChecked){  // Check Switch is active or not

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
                        // for set date and time at note change
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        val updateNote = Note(noteTitle,noteDescription,currentDate)
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

                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate))
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
                if (noteType == "Edit"){ // Update the Note

                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        val updateNote = Note(noteTitle,noteDescription,currentDate)
                        updateNote.id = noteId
                        noteViewModel.updateNote(updateNote)
                        Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
                    }

                }else{ // Add the New Note
                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate))
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
                etDate.isEnabled = true
                etTime.isEnabled = true


            }else{

                etTime.visibility = View.GONE
                etDate.visibility = View.GONE
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
                val myFormat = "dd-MM-yyyy"
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

            //for max date set (At current Time & Date)
//            dateDialog.datePicker.maxDate = cal1.timeInMillis
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

        // build the MaterialTimePicker Dialog
        mTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set Time")
            .build()

        // Show the MaterialTimePicker
        mTimePicker.show(supportFragmentManager,"Boss")

        // Action by Positive response "OK"
        mTimePicker.addOnPositiveButtonClickListener {
            val hour = mTimePicker.hour
            val minute = mTimePicker.minute
            etTime.setText(String.format("%02d : %02d",hour,minute))
        }

    }
}