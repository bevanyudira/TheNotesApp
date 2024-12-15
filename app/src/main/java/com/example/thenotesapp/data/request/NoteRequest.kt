package com.example.thenotesapp.data.request

import com.google.gson.annotations.SerializedName

data class NoteRequest(
    @SerializedName("noteTitle")
    val noteTitle: String,

    @SerializedName("noteDesc")
    val noteDesc: String
)
