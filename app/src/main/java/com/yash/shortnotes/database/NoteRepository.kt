package com.yash.shortnotes.database

import androidx.lifecycle.LiveData
import com.yash.shortnotes.model.Note

// NoteRepository interact with ViewModel
// Inside Repository we get all data (sqlite , other Apis ect)
// that data we have to use operation with viewModel
class NoteRepository(private val notesDao: NoteDao) {

    // on below line we are creating a variable for our list
    // and we are getting all the notes from our DAO class.
    val allNotes: LiveData<List<Note>> = notesDao.getAllNotes()

    // on below line we are creating an insert method
    // for adding the note to our database.
    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    // on below line we are creating a delete method
    // for deleting our note from database.
    suspend fun delete(note: Note){
        notesDao.delete(note)
    }

    // on below line we are creating a update method for
    // updating our note from database.
    suspend fun update(note: Note){
        notesDao.update(note)
    }

    suspend fun getMaxID() {
        notesDao.getMaxId()
    }

}