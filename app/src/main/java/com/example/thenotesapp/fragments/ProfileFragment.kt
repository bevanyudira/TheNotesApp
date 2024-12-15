package com.example.thenotesapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.thenotesapp.LoginActivity
import com.example.thenotesapp.R
import com.example.thenotesapp.databinding.FragmentProfileBinding
import com.example.thenotesapp.PrefManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize PrefManager
        prefManager = PrefManager.getInstance(requireContext())

        // Check if user is logged in
        if (prefManager.isLoggedIn()) {
            // Fetch user data from PrefManager
            val username = prefManager.getUsername()
            val name = prefManager.getName()

            // Display the username and name
            binding.userNameTextView.text = username
        } else {
            // If not logged in, display message or prompt login
            binding.userNameTextView.text = "Not Logged In"
        }

        // Handle logout action
        binding.logoutButton.setOnClickListener {
            // Clear login status
            prefManager.setLogin(false)
            prefManager.clearUserSession()

            // Navigate to LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)

            // Finish current activity (MainActivity) so the user can't return after logout
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}