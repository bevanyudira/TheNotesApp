package com.example.thenotesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.thenotesapp.model.Note

@Dao
interface NoteDao {

    // Menyisipkan catatan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    // Mengupdate catatan
    @Update
    suspend fun updateNote(note: Note)

    // Menghapus catatan
    @Delete
    suspend fun deleteNote(note: Note)

    // Mengambil semua catatan
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    // Mencari catatan berdasarkan query
    @Query("SELECT * FROM notes WHERE noteTitle LIKE :query OR noteDesc LIKE :query")
    fun searchNote(query: String?): LiveData<List<Note>>

    // Mengambil catatan yang di-bookmark
    @Query("SELECT * FROM notes WHERE isBookmarked = 1 ORDER BY id DESC")
    fun getBookmarkedNotes(): LiveData<List<Note>>

    // Mengupdate status bookmark
    @Query("UPDATE notes SET isBookmarked = :isBookmarked WHERE id = :noteId")
    suspend fun updateBookmarkStatus(noteId: Int, isBookmarked: Boolean)

}