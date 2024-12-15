package com.example.thenotesapp.model

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)