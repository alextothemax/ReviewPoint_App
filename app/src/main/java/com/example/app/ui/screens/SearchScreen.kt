package com.example.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.ReviewerRepository
import com.example.app.ui.components.*
import com.example.app.ui.theme.*

@Composable
fun SearchScreen(
    onSubjectClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val topReviewers = remember { ReviewerRepository.getTopReviewers() }
    val allReviewers = ReviewerRepository.reviewers

    val topFourSubjects = remember {
        val rankedSubjectNames = ReviewerRepository.getTopSubjects(4)

        val topSubjects = rankedSubjectNames.mapNotNull { name ->
            allAvailableSubjects.find { it.name == name }
        }.toMutableList()

        if (topSubjects.size < 4) {
            allAvailableSubjects.forEach { subject ->
                if (topSubjects.size < 4 && !topSubjects.any { it.name == subject.name }) {
                    topSubjects.add(subject)
                }
            }
        }
        topSubjects.toList()
    }

    val filteredReviewers = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            emptyList()
        } else {
            allReviewers.filter { reviewer ->
                reviewer.subject.contains(searchQuery, ignoreCase = true) ||
                reviewer.title.contains(searchQuery, ignoreCase = true) ||
                reviewer.uploaderName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReviewRed)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextOnRed)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Reviewer Library",
                color = TextOnRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
            }
            
            if (searchQuery.isEmpty()) {
                item {
                    TopSubjectsSection(
                        onSubjectClick = onSubjectClick,
                        displaySubjects = topFourSubjects
                    )
                }
                item {
                    LatestReviewersHeader(
                        title = "Top Reviewers",
                        actionText = ""
                    )
                }
                items(topReviewers) { reviewer ->
                    ReviewerCard(reviewer)
                }
            } else {
                item {
                    Text(
                        text = "Search Results",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (filteredReviewers.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val emptyMessage = when {
                                !allReviewers.any { it.uploaderName.contains(searchQuery, ignoreCase = true) } -> 
                                    "No author named \"$searchQuery\" is available."
                                !allReviewers.any { it.subject.contains(searchQuery, ignoreCase = true) } ->
                                    "No Reviewer is Currently Available for this subject."
                                else -> "No Reviewers matching your search were found."
                            }
                            
                            Text(
                                text = emptyMessage,
                                textAlign = TextAlign.Center,
                                color = TextSecondary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    items(filteredReviewers) { reviewer ->
                        ReviewerCard(reviewer)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search by title, subject, or author...", color = TextMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear search", tint = TextSecondary)
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = ReviewRed,
            unfocusedBorderColor = Color.LightGray,
            focusedPlaceholderColor = TextMuted,
            unfocusedPlaceholderColor = TextMuted
        )
    )
}
