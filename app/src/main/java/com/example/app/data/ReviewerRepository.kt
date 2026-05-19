package com.example.app.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.FileProvider
import com.example.app.model.Badge
import com.example.app.model.Reviewer
import java.io.File
import java.io.FileOutputStream

object ReviewerRepository {
    private val _reviewers = mutableStateListOf<Reviewer>(
        Reviewer("1", "Calculus Basics", "Mathematics", "Jane Doe", "1 day ago", 500, 1200, 4.8, Badge.ACADEMIC_AUTHORITY, System.currentTimeMillis() - 86400000),
        Reviewer("2", "Python Intro", "ICT / Computer", "Charlie Brown", "2 days ago", 600, 1500, 4.9, Badge.REVIEWPOINT_TITAN, System.currentTimeMillis() - 172800000),
        Reviewer("3", "World History", "Social Studies / History", "Alice Lee", "10 hours ago", 450, 950, 4.6, Badge.KNOWLEDGE_CRAFTER, System.currentTimeMillis() - 36000000)
    )

    val reviewers: List<Reviewer> get() = _reviewers

    fun addReviewer(reviewer: Reviewer) {
        _reviewers.add(0, reviewer) // Add to top
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

    fun downloadReviewer(context: Context, reviewer: Reviewer) {
        val index = _reviewers.indexOfFirst { it.id == reviewer.id }
        if (index != -1) {
            val current = _reviewers[index]
            _reviewers[index] = current.copy(downloads = current.downloads + 1)
        }
        
        // Handle opening the file if it exists locally
        if (reviewer.localFileName != null) {
            openReviewerFile(context, reviewer.localFileName)
        } else {
            Toast.makeText(context, "File data not available for this sample", Toast.LENGTH_SHORT).show()
        }
    }

    fun rateReviewer(reviewerId: String, newRating: Double) {
        val index = _reviewers.indexOfFirst { it.id == reviewerId }
        if (index != -1) {
            val reviewer = _reviewers[index]
            _reviewers[index] = reviewer.copy(rating = newRating)
        }
    }

    private fun openReviewerFile(context: Context, fileName: String) {
        try {
            val file = File(File(context.filesDir, "reviewers"), fileName)
            if (!file.exists()) {
                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
                return
            }

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            context.startActivity(Intent.createChooser(intent, "Open Reviewer"))
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Helper to save file to internal storage
    fun saveFileToInternal(context: Context, uri: Uri): String? {
        return try {
            val contentResolver = context.contentResolver
            val fileName = "reviewer_${System.currentTimeMillis()}.${getFileExtension(context, uri)}"
            val folder = File(context.filesDir, "reviewers")
            if (!folder.exists()) folder.mkdirs()
            
            val destFile = File(folder, fileName)
            
            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            fileName
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
            ?: uri.path?.substringAfterLast(".", "pdf")
    }
}
