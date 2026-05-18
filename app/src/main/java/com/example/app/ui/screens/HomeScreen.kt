package com.example.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.ReviewerRepository
import com.example.app.data.UserRepository
import com.example.app.model.Reviewer
import com.example.app.ui.components.*
import com.example.app.ui.theme.ReviewRed
import java.util.*

@Composable
fun HomeScreen(
    onSubjectClick: (String) -> Unit,
    onViewLatestReviewers: () -> Unit,
    onAboutClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    // Database Logic: Get Top Reviewers sorted by Ranking (Stars) and Downloads
    val topReviewers = remember { ReviewerRepository.getTopReviewers() }

    // Database Logic: Determine top subjects based on most reviewers available
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

    val quotes = listOf(
        "Start now—your future self will thank you.",
        "Small progress is still progress. Keep going.",
        "Focus today, succeed tomorrow.",
        "Discipline beats motivation every time.",
        "One step forward is still forward.",
        "Consistency creates confidence.",
        "Do it now, not later.",
        "Your goals need action, not excuses.",
        "Keep showing up—that’s how you win.",
        "Study smart, not just hard.",
        "You’re capable of more than you think.",
        "Every effort counts. Don’t stop.",
        "Make today productive.",
        "Push through—it will be worth it.",
        "Progress over perfection. Always.",
        "Stay focused. You got this.",
        "Success starts with showing up.",
        "Turn pressure into performance.",
        "Keep going—don’t break your streak.",
        "You’re building something bigger.",
        "Sacrifice now, enjoy later.",
        "Make your time count.",
        "Don’t quit—improve.",
        "One focused hour can change everything.",
        "Stay consistent, stay winning.",
        "Effort today, success tomorrow.",
        "Believe it. Work for it. Achieve it.",
        "Less scrolling, more studying.",
        "You’re closer than you think.",
        "Finish strong—no excuses."
    )

    val quoteIndex = remember {
        val calendar = Calendar.getInstance()
        val daysSinceEpoch = (calendar.timeInMillis / (1000 * 60 * 60 * 24)).toInt()
        daysSinceEpoch % quotes.size
    }
    val currentQuote = quotes[quoteIndex]
    val currentUser by UserRepository.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        HeaderSection(onAboutClick = onAboutClick)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    WelcomeSection(name = currentUser?.name?.split(" ")?.firstOrNull() ?: "Guest")
                    Spacer(modifier = Modifier.height(8.dp))
                    QuoteSection(quote = currentQuote)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            item {
                HomeSearchBar(onSearchClick = onSearchClick)
            }

            item {
                TopSubjectsSection(
                    onSubjectClick = onSubjectClick,
                    displaySubjects = topFourSubjects
                )
            }
            item {
                LatestReviewersHeader(
                    title = "Top Reviewers",
                    actionText = "Latest Reviewers",
                    onViewAll = onViewLatestReviewers
                )
            }
            items(topReviewers) { reviewer ->
                ReviewerCard(reviewer)
            }
        }
    }
}

@Composable
fun HeaderSection(onAboutClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ReviewRed, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .clickable { onAboutClick() }
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ReviewPoint",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun QuoteSection(quote: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = ReviewRed.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = quote,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun WelcomeSection(name: String) {
    Text(
        text = "Welcome, $name!",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeSearchBar(onSearchClick: () -> Unit) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSearchClick() },
        enabled = false,
        placeholder = { Text("Search") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledContainerColor = Color.White,
            disabledBorderColor = Color.LightGray,
            disabledLeadingIconColor = Color.Gray,
            disabledPlaceholderColor = Color.Gray
        )
    )
}
