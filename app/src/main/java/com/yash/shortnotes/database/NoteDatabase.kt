package com.yash.shortnotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yash.shortnotes.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)// Inside the database multiple entity or table like Note
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NoteDao

    companion object {
        // Singleton prevents multiple
        // instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "note_database").build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}