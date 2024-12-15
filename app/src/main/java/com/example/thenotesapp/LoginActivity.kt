package com.example.thenotesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thenotesapp.databinding.ActivityLoginBinding
import com.example.thenotesapp.model.Users
import com.example.thenotesapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val apiService by lazy { ApiClient.getInstance() }
    private val prefManager by lazy { PrefManager.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkLoginStatus()
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            login.setOnClickListener {
                if (validateInputs()) {
                    loginUser(
                        username = username.text.toString().trim(),
                        password = password.text.toString()
                    )
                }
            }

            toRegister.setOnClickListener {
                navigateToRegister()
            }
        }
    }

    private fun validateInputs(): Boolean {
        with(binding) {
            val username = username.text.toString().trim()
            val password = password.text.toString()

            // Reset error states
            usernameLayout.error = null
            passwordLayout.error = null

            when {
                username.isEmpty() -> {
                    usernameLayout.error = "Username harus diisi"
                    return false
                }
                password.isEmpty() -> {
                    passwordLayout.error = "Password harus diisi"
                    return false
                }
            }
            return true
        }
    }

    private fun loginUser(username: String, password: String) {
        apiService.getAllUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        val user = users.find { it.username == username && it.password == password }
                        if (user != null) {
                            saveUserData(user)  // Langsung save data user jika ditemukan
                        } else {
                            showToast("Username atau password salah")
                        }
                    } else {
                        showToast("Gagal memuat data pengguna")
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        500 -> "Terjadi kesalahan pada server"
                        else -> "Gagal melakukan login"
                    }
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                showToast("Gagal terhubung ke server")
            }
        })
    }

    private fun saveUserData(user: Users) {
        try {
            prefManager.apply {
                setLogin(true)  // Set login status
                saveUserSession(
                    username = user.username,
                    name = user.name
                )
            }
            navigateToMain()
        } catch (e: Exception) {
            showToast("Gagal menyimpan data user: ${e.message}")
        }
    }

    private fun checkLoginStatus() {
        if (prefManager.isLoggedIn()) {
            navigateToMain()
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }
}