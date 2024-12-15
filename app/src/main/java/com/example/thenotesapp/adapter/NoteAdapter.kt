package com.example.thenotesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.thenotesapp.R
import com.example.thenotesapp.databinding.NoteLayoutBinding
import com.example.thenotesapp.fragments.HomeFragmentDirections
import com.example.thenotesapp.model.Note
import com.example.thenotesapp.viewmodel.NoteViewModel

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notesViewModel: NoteViewModel? = null

    fun setNoteViewModel(viewModel: NoteViewModel) {
        notesViewModel = viewModel
    }

    class NoteViewHolder(val itemBinding: NoteLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.noteTitle.text = currentNote.noteTitle
        holder.itemBinding.noteDesc.text = currentNote.noteDesc

        // Set the bookmark icon based on the bookmark status
        val bookmarkIcon = if (currentNote.isBookmarked) {
            R.drawable.ic_bookmark_filled
        } else {
            R.drawable.ic_bookmark_border
        }
        holder.itemBinding.bookmarkIcon.setImageResource(bookmarkIcon)

        // Handle bookmark click to toggle bookmark status
        holder.itemBinding.bookmarkIcon.setOnClickListener {
            currentNote.isBookmarked = !currentNote.isBookmarked  // Toggle bookmark status

            // Update UI untuk menunjukkan perubahan
            notifyItemChanged(position)
            
        }

        // Navigate to EditNoteFragment on note click
        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }
}