package com.yash.shortnotes.database

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import com.yash.shortnotes.model.Note
import org.jetbrains.annotations.NotNull
import java.sql.RowId

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

//    @NotNull
//    suspend fun getMaxID(i:Int){
//        notesDao.getMaxId(i)
//    }

}