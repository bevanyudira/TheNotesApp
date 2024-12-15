package com.example.thenotesapp.network

import com.example.thenotesapp.data.request.NoteRequest
import com.example.thenotesapp.data.response.NoteResponse
import com.example.thenotesapp.data.response.RegisterResponse
import com.example.thenotesapp.model.Note
import com.example.thenotesapp.model.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("notes")
    fun getAllNotes(): Call<List<Note>>

    @POST("notes")
    fun createNote(@Body request: NoteRequest): Call<NoteResponse>

    @POST("notes/{id}")
    fun updateNote(@Path("id") id: String, @Body request: NoteRequest): Call<NoteResponse>

    @DELETE("notes/{id}")
    fun deleteNote(@Path("id") id: String): Call<Void>

    @GET("account")
    fun getAllUsers(): Call<List<Users>>

    @POST("account")
    fun createUser(@Body request: Users): Call<RegisterResponse>

}