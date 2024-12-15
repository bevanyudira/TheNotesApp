package com.example.thenotesapp.repository

import com.example.thenotesapp.database.NoteDatabase
import com.example.thenotesapp.model.Note
import com.example.thenotesapp.network.ApiService
import com.example.thenotesapp.data.request.NoteRequest
import com.example.thenotesapp.data.response.NoteResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteRepository(
    private val db: NoteDatabase,
    private val apiService: ApiService
) {
    // Existing local database functions
    suspend fun insertNote(note: Note) = db.getNoteDao().insertNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)
    fun getAllNotes() = db.getNoteDao().getAllNotes()
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)
    fun getBookmarkedNotes() = db.getNoteDao().getBookmarkedNotes()

    // API functions
    private val _apiNotes = MutableLiveData<List<Note>>()
    val apiNotes: LiveData<List<Note>> = _apiNotes

    fun fetchNotesFromApi(callback: (Boolean) -> Unit) {
        apiService.getAllNotes().enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    _apiNotes.postValue(response.body())
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun createNoteApi(note: Note, callback: (Boolean) -> Unit) {
        val request = NoteRequest(
            noteTitle = note.noteTitle,
            noteDesc = note.noteDesc
        )
        apiService.createNote(request).enqueue(object : Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>, response: Response<NoteResponse>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updateNoteApi(note: Note, callback: (Boolean) -> Unit) {
        val request = NoteRequest(
            noteTitle = note.noteTitle,
            noteDesc = note.noteDesc
        )
        apiService.updateNote(note.id.toString(), request).enqueue(object : Callback<NoteResponse> {
            override fun onResponse(call: Call<NoteResponse>, response: Response<NoteResponse>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun deleteNoteApi(noteId: String, callback: (Boolean) -> Unit) {
        apiService.deleteNote(noteId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}