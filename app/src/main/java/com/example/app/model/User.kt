package com.example.app.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePic: String? = null,
    val uploadsCount: Int = 0,
    val averageRating: Double = 0.0
) {
    val currentBadge: Badge?
        get() = Badge.entries.lastOrNull { 
            uploadsCount >= it.uploadsRequired && averageRating >= it.minRating 
        }
}
