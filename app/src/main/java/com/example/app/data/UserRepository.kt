package com.example.app.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

data class User(
    val name: String = "Jane Doe",
    val email: String = "jane.doe@example.com"
)

object UserRepository {
    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser: State<User?> = _currentUser

    fun login(name: String, email: String) {
        _currentUser.value = User(name, email)
    }

    fun logout() {
        _currentUser.value = null
    }
}
