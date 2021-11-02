package com.example.notesappfirebase

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.muddz.styleabletoast.StyleableToast

class MainActivity : AppCompatActivity() {

    private val myViewModel by lazy { ViewModelProvider(this).get(MyViewModel::class.java) }

    private lateinit var mainRV: RecyclerView
    private lateinit var noteEntry: EditText
    private lateinit var saveButton: Button
    private lateinit var adapter: RVAdapter
    private lateinit var notes: ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRV= findViewById(R.id.rvMain)
        noteEntry= findViewById(R.id.noteEntry)
        saveButton= findViewById(R.id.saveButton)

        notes= arrayListOf()
        adapter= RVAdapter(this,notes)
        mainRV.adapter= adapter
        mainRV.layoutManager= LinearLayoutManager(this)

       myViewModel.gettingNotes().observe(this){
                notesList ->
            notes.clear()
            notes.addAll(notesList)
            adapter.notifyDataSetChanged()
        }

        saveButton.setOnClickListener{
            if (noteEntry.text.isNotBlank()){
                myViewModel.addNote(Data("",noteEntry.text.toString()))
                StyleableToast.makeText(this@MainActivity, "Saved Successfully!!", R.style.myToast).show()
                noteEntry.text.clear()
                noteEntry.clearFocus()
            }
            else
                StyleableToast.makeText(this,"Please Enter Valid Values!!",R.style.myToast).show()
        }

    }

    fun update(note: Data) {
        val input = EditText(this)
        input.hint = "Enter New Note Here"
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Please Enter New Note:")
            .setCancelable(false)
            .setPositiveButton("Save") {
                    _,_ -> val str =input.text.toString()
                if(str.isNotBlank()) {
                    myViewModel.updateNote(Data(note.key,str))
                    StyleableToast.makeText(this, "Update Successfully!!", R.style.myToast).show()
                }
                else
                    StyleableToast.makeText(this,"Please Enter Valid Values!!",R.style.myToast).show()
            }
            .setNegativeButton("Cancel") {dialog,_ -> dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(input)
        alert.show()
    }

    fun delete(note: Data) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Yes") {
                    _,_ ->
                myViewModel.deleteNote(note)
                StyleableToast.makeText(this, "Deleted Successfully!!", R.style.myToast).show()
            }
            .setNegativeButton("Cancel") {dialog,_ -> dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Are You Sure You Want to Delete Note")
        alert.show()
    }
}