package com.example.app.data

import androidx.compose.runtime.mutableStateListOf
import com.example.app.model.Badge
import com.example.app.model.Reviewer

object ReviewerRepository {
    private val _reviewers = mutableStateListOf<Reviewer>(
        Reviewer("1", "Calculus Basics", "Mathematics", "Jane Doe", "1 day ago", 500, 1200, 4.8, Badge.ACADEMIC_AUTHORITY, System.currentTimeMillis() - 86400000),
        Reviewer("2", "Python Intro", "ICT / Computer", "Charlie Brown", "2 days ago", 600, 1500, 4.9, Badge.REVIEWPOINT_TITAN, System.currentTimeMillis() - 172800000),
        Reviewer("3", "World History", "Social Studies / History", "Alice Lee", "10 hours ago", 450, 950, 4.6, Badge.KNOWLEDGE_CRAFTER, System.currentTimeMillis() - 36000000),
        Reviewer("4", "English Grammar", "English", "Bob Wilson", "3 days ago", 300, 800, 4.2, Badge.FIRST_SPARK, System.currentTimeMillis() - 259200000),
        Reviewer("5", "Chemistry Study Guide", "Science", "John Smith", "7 hours ago", 200, 500, 4.5, Badge.RISING_CONTRIBUTOR, System.currentTimeMillis() - 25200000),
        Reviewer("6", "General Physics", "Science", "John Smith", "5 days ago", 150, 400, 4.0, Badge.KNOWLEDGE_CRAFTER, System.currentTimeMillis() - 432000000)
    )

    val reviewers: List<Reviewer> get() = _reviewers

    fun addReviewer(reviewer: Reviewer) {
        _reviewers.add(reviewer)
    }

    fun getSortedReviewers(): List<Reviewer> {
        return _reviewers.sortedWith(
            compareBy<Reviewer> { it.subject }
                .thenBy { it.uploaderName }
                .thenByDescending { it.timestamp }
        )
    }

    fun getTopSubjects(limit: Int = 4): List<String> {
        return _reviewers.groupBy { it.subject }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }

    fun getTopReviewers(): List<Reviewer> {
        return _reviewers.sortedWith(
            compareByDescending<Reviewer> { it.rating }
                .thenByDescending { it.downloads }
        )
    }

    fun getLatestReviewers(): List<Reviewer> {
        return _reviewers.sortedByDescending { it.timestamp }
    }
}
