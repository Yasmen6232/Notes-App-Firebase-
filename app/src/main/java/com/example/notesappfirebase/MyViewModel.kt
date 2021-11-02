package com.example.notesappfirebase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(application: Application): AndroidViewModel(application) {

    private val connection= FireBase(application)

    fun gettingNotes(): LiveData<List<Data>>{
        return connection.gettingData()
    }

    fun addNote(note: Data){
        CoroutineScope(IO).launch {
            connection.addNewNote(note)
        }
    }

    fun updateNote(note: Data){
        CoroutineScope(IO).launch {
            connection.updateNote(note)
        }
    }

    fun deleteNote(note: Data){
        CoroutineScope(IO).launch {
            connection.deleteNote(note)
        }
    }
}