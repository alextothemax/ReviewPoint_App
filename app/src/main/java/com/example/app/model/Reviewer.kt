package com.example.app.model

data class Reviewer(
    val id: String,
    val title: String,
    val subject: String,
    val uploaderName: String,
    val uploadTimeAgo: String,
    val downloads: Int,
    val views: Int,
    val rating: Double,
    val uploaderBadge: Badge? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class Badge(val title: String, val uploadsRequired: Int, val minRating: Double = 0.0) {
    FIRST_SPARK("First Spark", 1, 0.0),
    RISING_CONTRIBUTOR("Rising Contributor", 10, 3.5),
    KNOWLEDGE_CRAFTER("Knowledge Crafter", 30, 4.0),
    ACADEMIC_AUTHORITY("Academic Authority", 60, 4.5),
    REVIEWPOINT_TITAN("ReviewPoint Titan", 100, 4.8)
}
