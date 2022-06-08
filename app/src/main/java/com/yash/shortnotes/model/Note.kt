package com.yash.shortnotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//
//@Entity(tableName = "notesTable")
//class Note (
//    @ColumnInfo(name = "title") val noteTitle: String,
//    @ColumnInfo(name = "description") val noteDescription : String,
//    @ColumnInfo(name = "timestamp") val timeStamp: String,
//    @ColumnInfo(name = "alertTimes") val alertTime: String
//){
//    @PrimaryKey(autoGenerate = true)
//    var id = 0
//
//}

@Entity(tableName = "notesTable")
data class Note(val title: String,
                val description: String,
                val timestamp: String,
                val alertTimes : String,
                @PrimaryKey(autoGenerate = true) var id: Int? = null)