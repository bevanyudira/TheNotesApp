package com.example.thenotesapp

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PrefManager private constructor(context: Context) {
    private val sharedPref: SharedPreferences

    companion object {
        private const val PREF_NAME = "TheNotesApp_Pref"
        private const val KEY_IS_LOGIN = "key_is_login"
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_USERNAME = "key_username"
        private const val KEY_NAME = "key_name"
        private const val KEY_LAST_LOGIN = "key_last_login"

        @Volatile
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserSession(username: String, name: String) {
        sharedPref.edit {
            putBoolean(KEY_IS_LOGIN, true)
            putString(KEY_USERNAME, username)
            putString(KEY_NAME, name)
            putLong(KEY_LAST_LOGIN, System.currentTimeMillis())
        }
    }

    fun clearUserSession() {
        sharedPref.edit {
            remove(KEY_IS_LOGIN)
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            remove(KEY_NAME)
            remove(KEY_LAST_LOGIN)
        }
    }

    // Login status
    fun setLogin(isLogin: Boolean) {
        sharedPref.edit {
            putBoolean(KEY_IS_LOGIN, isLogin)
            if (!isLogin) {
                clearUserSession()
            }
        }
    }

    fun isLoggedIn(): Boolean = sharedPref.getBoolean(KEY_IS_LOGIN, false)

    // User ID
    fun setUserId(userId: String) {
        sharedPref.edit { putString(KEY_USER_ID, userId) }
    }

    fun getUserId(): String? = sharedPref.getString(KEY_USER_ID, null)

    // Username
    fun setUsername(username: String) {
        sharedPref.edit { putString(KEY_USERNAME, username) }
    }

    fun getUsername(): String? = sharedPref.getString(KEY_USERNAME, null)

    // Name
    fun setName(name: String) {
        sharedPref.edit { putString(KEY_NAME, name) }
    }

    fun getName(): String? = sharedPref.getString(KEY_NAME, null)

    // Last login time
    private fun setLastLoginTime() {
        sharedPref.edit { putLong(KEY_LAST_LOGIN, System.currentTimeMillis()) }
    }

    fun getLastLoginTime(): Long = sharedPref.getLong(KEY_LAST_LOGIN, 0)

    // Check if session is valid (optional, can be used for session timeout)
    fun isSessionValid(): Boolean {
        val lastLoginTime = getLastLoginTime()
        val currentTime = System.currentTimeMillis()
        val sessionTimeout = 24 * 60 * 60 * 1000 // 24 hours in milliseconds

        return currentTime - lastLoginTime < sessionTimeout
    }

    // Get all user data as a map
    fun getUserData(): Map<String, String?> {
        return mapOf(
            "userId" to getUserId(),
            "username" to getUsername(),
            "name" to getName()
        )
    }

    // Clear all preferences
    fun clearAll() {
        sharedPref.edit { clear() }
    }
}