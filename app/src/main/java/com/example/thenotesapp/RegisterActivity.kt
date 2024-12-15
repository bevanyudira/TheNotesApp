package com.example.thenotesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thenotesapp.data.response.RegisterResponse
import com.example.thenotesapp.databinding.ActivityRegisterBinding
import com.example.thenotesapp.model.Users
import com.example.thenotesapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val apiService by lazy { ApiClient.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            register.setOnClickListener {
                if (validateInputs()) {
                    val users = Users(
                        id = System.currentTimeMillis().toString(),
                        name = name.text.toString().trim(),
                        username = username.text.toString().trim(),
                        password = password.text.toString()
                    )
                    registerUser(users)
                }
            }

            toLogin.setOnClickListener {
                navigateToLogin()
            }
        }
    }

    private fun validateInputs(): Boolean {
        with(binding) {
            val name = name.text.toString().trim()
            val username = username.text.toString().trim()
            val password = password.text.toString()

            // Reset error states
            nameLayout.error = null
            usernameLayout.error = null
            passwordLayout.error = null

            when {
                name.isEmpty() -> {
                    nameLayout.error = "Nama lengkap harus diisi"
                    return false
                }
                name.length < 3 -> {
                    nameLayout.error = "Nama minimal 3 karakter"
                    return false
                }
                username.isEmpty() -> {
                    usernameLayout.error = "Username harus diisi"
                    return false
                }
                username.length < 4 -> {
                    usernameLayout.error = "Username minimal 4 karakter"
                    return false
                }
                password.isEmpty() -> {
                    passwordLayout.error = "Password harus diisi"
                    return false
                }
                password.length < 6 -> {
                    passwordLayout.error = "Password minimal 6 karakter"
                    return false
                }
            }
            return true
        }
    }

    private fun registerUser(users: Users) {

        apiService.createUser(users).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    showToast(data?.message ?: "Registrasi berhasil")
                    navigateToLogin()
                } else {
                    val errorMessage = when (response.code()) {
                        409 -> "Username sudah digunakan"
                        500 -> "Terjadi kesalahan pada server"
                        else -> "Gagal melakukan registrasi"
                    }
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showToast("Gagal terhubung ke server")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}