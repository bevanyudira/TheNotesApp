package com.example.thenotesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thenotesapp.model.Note
import com.example.thenotesapp.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(
    app: Application,
    private val noteRepository: NoteRepository
) : AndroidViewModel(app) {
    // Existing local database functions
    fun addNote(note: Note) = viewModelScope.launch {
        noteRepository.insertNote(note)
    }
    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepository.deleteNote(note)
    }
    fun updateNote(note: Note) = viewModelScope.launch {
        noteRepository.updateNote(note)
    }
    fun getAllNotes() = noteRepository.getAllNotes()
    fun searchNote(query: String?) = noteRepository.searchNote(query)
    fun getBookmarkedNotes() = noteRepository.getBookmarkedNotes()

    // API related functions
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchNotesFromApi() {
        _isLoading.value = true
        noteRepository.fetchNotesFromApi { success ->
            _isLoading.value = false
            if (!success) {
                _error.value = "Failed to fetch notes from API"
            }
        }
    }

    fun createNoteApi(note: Note) {
        _isLoading.value = true
        noteRepository.createNoteApi(note) { success ->
            _isLoading.value = false
            if (!success) {
                _error.value = "Failed to create note in API"
            }
        }
    }

    fun updateNoteApi(note: Note) {
        _isLoading.value = true
        noteRepository.updateNoteApi(note) { success ->
            _isLoading.value = false
            if (!success) {
                _error.value = "Failed to update note in API"
            }
        }
    }

    fun deleteNoteApi(noteId: String) {
        _isLoading.value = true
        noteRepository.deleteNoteApi(noteId) { success ->
            _isLoading.value = false
            if (!success) {
                _error.value = "Failed to delete note from API"
            }
        }
    }
}