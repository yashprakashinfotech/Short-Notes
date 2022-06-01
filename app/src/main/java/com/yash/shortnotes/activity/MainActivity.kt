package com.yash.shortnotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yash.shortnotes.R
import com.yash.shortnotes.adapter.NoteAdapter
import com.yash.shortnotes.adapter.NoteClickDeleteInterface
import com.yash.shortnotes.adapter.NoteClickInterface
import com.yash.shortnotes.helper.KeyClass
import com.yash.shortnotes.model.Note
import com.yash.shortnotes.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity(), NoteClickDeleteInterface, NoteClickInterface {

    private lateinit var fabNote : FloatingActionButton
    private lateinit var recyclerNote : RecyclerView

    private lateinit var noteViewModel : NoteViewModel
    private lateinit var noteAdapter : NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        // Live data show using life cycle observer
        noteViewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                noteAdapter.updateList(it)
//                Log.d("Hello",it.toString())
            }
        })
        fabNote.setOnClickListener {
            val i = Intent(this,AddEditActivity::class.java)
            startActivity(i)
//            this.finish()
        }

    }

    private fun initView() {
        fabNote = findViewById(R.id.fabAdd)
        recyclerNote = findViewById(R.id.noteRV)
//        recyclerNote.layoutManager = LinearLayoutManager(this)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerNote.layoutManager = linearLayoutManager

        noteAdapter = NoteAdapter(this,this,this)
        recyclerNote.adapter = noteAdapter
        noteViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NoteViewModel::class.java]

    }

    override fun onNoteClick(note: Note) {
        val i = Intent(this,AddEditActivity::class.java)
        i.putExtra(KeyClass.KEY_NOTE_TYPE,"Edit")
        i.putExtra(KeyClass.KEY_NOTE_TITLE,note.noteTitle)
        i.putExtra(KeyClass.KEY_NOTE_DESCRIPTION,note.noteDescription)
        i.putExtra(KeyClass.KEY_NOTE_ALERT_TIME,note.alertTime)
        i.putExtra(KeyClass.KEY_NOTE_ID,note.id)
        startActivity(i)
    }

    override fun onDeleteIconClick(note: Note) {
        noteViewModel.deleteNote(note)
        Toast.makeText(this,"${note.noteTitle} Delete", Toast.LENGTH_SHORT).show()
    }
}