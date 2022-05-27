package com.yash.shortnotes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.yash.shortnotes.R
import com.yash.shortnotes.model.Note

class NoteAdapter(val context: Context,
                  private val noteClickDeleteInterface: NoteClickDeleteInterface,
                  private val noteClickInterface: NoteClickInterface) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val allNote = ArrayList<Note>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item,parent,false)
        return NoteViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val notesPosition = allNote[position]
        holder.txtNoteTitle.text = notesPosition.noteTitle
        holder.txtTimeStamp.text = "Last Update : "+ notesPosition.timeStamp

        holder.icDelete.setOnClickListener {

            val deleteNoteAlertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
            deleteNoteAlertDialog.setTitle("Alert!")
            deleteNoteAlertDialog.setMessage("Are you sure to Delete the Note: ${notesPosition.noteTitle}?")
            deleteNoteAlertDialog.setPositiveButton("Yes") { dialog, _ ->
                noteClickDeleteInterface.onDeleteIconClick(notesPosition)
                dialog.dismiss() }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            deleteNoteAlertDialog.show()


//            noteClickDeleteInterface.onDeleteIconClick(notesPosition)
        }
        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(notesPosition)
        }
    }

    override fun getItemCount(): Int {
        return allNote.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList : List<Note>){
        allNote.clear()
        allNote.addAll(newList)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtNoteTitle : TextView = itemView.findViewById(R.id.txtNoteTitle)
        val txtTimeStamp : TextView = itemView.findViewById(R.id.txtTimeStamp)
        val icDelete : ImageView = itemView.findViewById(R.id.icDelete)
    }
}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}