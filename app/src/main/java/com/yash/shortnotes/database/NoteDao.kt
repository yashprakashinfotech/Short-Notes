package com.yash.shortnotes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yash.shortnotes.model.Note

@Dao
interface NoteDao {

    // adding a new entry to our database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note : Note)     // suspend  function is allow to execute function in background thread

    // for deleting our note.
    @Delete
    suspend fun delete(note: Note)

    // below is the method to read all the notes
    // we have to get the data.
    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>   // Live data function execute in background thread

    // below method is use to update the note.
    @Update
    suspend fun update(note: Note)

    @Query("SELECT id from notesTable order by id DESC limit 1")
    suspend fun getMaxId() : Int
}