package com.example.notesappfirebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappfirebase.databinding.NotesViewBinding

class RVAdapter (private val context: MainActivity,private val list: ArrayList<Data>): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: NotesViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(NotesViewBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note= list[position]
        holder.binding.noteTV.text= note.note
        holder.binding.editImage.setOnClickListener{
           context.update(note)
        }
        holder.binding.deleteImage.setOnClickListener{
            context.delete(note)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}