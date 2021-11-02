package com.example.notesappfirebase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.muddz.styleabletoast.StyleableToast


class FireBase (private val context: Context) {

    private val firebase = Firebase.firestore
    private val liveData: MutableLiveData<List<Data>> = MutableLiveData<List<Data>>()

    fun gettingData(): LiveData<List<Data>>{
        FirebaseFirestore.getInstance()
            .collection("Notes")
            .addSnapshotListener{
                snapshot, e ->
                //if there is an exception we want to skip
                if (e != null){
                    Log.d("MyData","Error: $e")
                    StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                    return@addSnapshotListener
                }
                if (snapshot != null){
                    //we have a populated snapshot
                    val list= arrayListOf<Data>()

                    val document= snapshot.documents
                    document.forEach{
                        // getting the note and save it
                        val data= it.getString("Note")
                        if (data != null)
                            list.add(Data(it.id,data))
                    }
                    liveData.value = list
                }
            }
        return liveData
    }

    fun addNewNote(note: Data){
        // Create a new note with a pk and the note
        val newNote = hashMapOf("Note" to note.note)

        // Add a new document with a generated ID
        firebase.collection("Notes")
            .add(newNote)
            .addOnSuccessListener { documentReference ->
                StyleableToast.makeText(context, "Add Success", R.style.myToast).show()
                Log.d("MyData", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                Log.w("MyData", "Error adding document", e)
            }
    }

    fun updateNote(note: Data){
        firebase.collection("Notes")
            .document(note.key)
            .update("Note", note.note)
            .addOnSuccessListener {
                StyleableToast.makeText(context, "Updated Success", R.style.myToast).show()
                Log.d("MyData", "Updated ID: ${note.key}")
            }
            .addOnFailureListener { e ->
                StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                Log.w("MyData", "Error Updating document", e)
            }
    }

    fun deleteNote(note: Data){
        firebase.collection("Notes")
            .document(note.key)
            .delete()
            .addOnSuccessListener {
                StyleableToast.makeText(context, "Deleted Success", R.style.myToast).show()
                Log.d("MyData", "Deleted ID: ${note.key}")
            }
            .addOnFailureListener { e ->
                StyleableToast.makeText(context, "Error\n$e", R.style.myToast).show()
                Log.w("MyData", "Error Deleting document", e)
            }
    }
}