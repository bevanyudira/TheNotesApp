package com.example.thenotesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.thenotesapp.database.NoteDatabase
import com.example.thenotesapp.repository.NoteRepository
import com.example.thenotesapp.viewmodel.NoteViewModel
import com.example.thenotesapp.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.thenotesapp.network.ApiClient

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel
    private lateinit var navController: NavController
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize PrefManager
        prefManager = PrefManager.getInstance(this)

        // Check if the user is logged in, if not, navigate to LoginActivity
        if (!prefManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        // Setup ViewModel
        setupViewModel()

        // Setup NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Setup Bottom Navigation
        setupBottomNavigation()
    }

    private fun setupViewModel() {
        val apiService = ApiClient.getInstance()
        val noteRepository = NoteRepository(
            db = NoteDatabase(this),
            apiService = apiService
        )

        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_bookmark -> {
                    navController.navigate(R.id.action_homeFragment_to_bookmarkFragment)
                    true
                }
                R.id.nav_profile -> {
                    if (prefManager.isLoggedIn()) {
                        navController.navigate(R.id.profileFragment)
                    } else {
                        navigateToLogin()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}