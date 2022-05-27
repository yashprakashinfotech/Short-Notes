package com.yash.shortnotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.yash.shortnotes.R
import com.yash.shortnotes.helper.KeyClass
import com.yash.shortnotes.model.Note
import com.yash.shortnotes.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEditActivity : AppCompatActivity() {

    private lateinit var etNoteTitle : EditText
    private lateinit var etNoteDescription : EditText
    private lateinit var btnAddUpdate : Button

    private lateinit var noteViewModel : NoteViewModel
    var noteId = -1

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

            if (noteType == "Edit"){

                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                    val currentDate: String = sdf.format(Date())
                    val updateNote = Note(noteTitle,noteDescription,currentDate)
                    updateNote.id = noteId
                    noteViewModel.updateNote(updateNote)
                    Toast.makeText(this,"Note Update", Toast.LENGTH_SHORT).show()
                }

            }else{
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                    val currentDate: String = sdf.format(Date())
                    noteViewModel.addNote(Note(noteTitle,noteDescription,currentDate))
                    Toast.makeText(this,"Note Added", Toast.LENGTH_SHORT).show()
                }
            }

            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }

    private fun initView() {

        etNoteTitle = findViewById(R.id.etNoteTitle)
        etNoteDescription = findViewById(R.id.etNoteDescription)
        btnAddUpdate = findViewById(R.id.btnAddUpdate)
        noteViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
    }
}