package com.example.thenotesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thenotesapp.R
import com.example.thenotesapp.adapter.NoteAdapter
import com.example.thenotesapp.databinding.FragmentBookmarkBinding
import com.example.thenotesapp.repository.NoteRepository
import com.example.thenotesapp.database.NoteDatabase
import com.example.thenotesapp.viewmodel.NoteViewModel
import com.example.thenotesapp.viewmodel.NoteViewModelFactory
import com.example.thenotesapp.network.ApiClient
import com.google.android.material.snackbar.Snackbar

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var binding: FragmentBookmarkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        observeData()
    }

    private fun setupViewModel() {
        val apiService = ApiClient.getInstance()
        val noteRepository = NoteRepository(
            db = NoteDatabase(requireContext()),
            apiService = apiService
        )
        val factory = NoteViewModelFactory(requireActivity().application, noteRepository)
        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        binding.bookmarkRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }
        noteAdapter.setNoteViewModel(noteViewModel)
    }

    private fun observeData() {
        // Observe bookmarked notes
        noteViewModel.getBookmarkedNotes().observe(viewLifecycleOwner) { notes ->
            binding.emptyBookmarkText.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
            noteAdapter.differ.submitList(notes)
        }

        // Observe errors
        noteViewModel.error.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}