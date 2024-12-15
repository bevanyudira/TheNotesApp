package com.example.thenotesapp.data.response

import com.example.thenotesapp.model.Users

data class RegisterResponse(
    val message: String,
    val status: Boolean,
    val data: Users? = null
)