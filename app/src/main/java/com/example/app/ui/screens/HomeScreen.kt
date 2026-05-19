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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.ReviewerRepository
import com.example.app.data.UserRepository
import com.example.app.ui.components.*
import com.example.app.ui.theme.*
import java.util.*

@Composable
fun HomeScreen(
    onSubjectClick: (String) -> Unit,
    onViewLatestReviewers: () -> Unit,
    onAboutClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val recentReviewers = remember { ReviewerRepository.getLatestReviewers() }

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
            .background(BackgroundWhite)
    ) {
        HeaderSection(onAboutClick = onAboutClick)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    WelcomeSection(name = currentUser?.name?.split(" ")?.firstOrNull() ?: "Guest")
                    QuoteSection(quote = currentQuote)
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
                    title = "Most Recent Reviewers",
                    actionText = "See all",
                    onViewAll = onViewLatestReviewers
                )
            }
            items(recentReviewers.take(5)) { reviewer ->
                ReviewerCard(reviewer)
            }
        }
    }
}

@Composable
fun HeaderSection(onAboutClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAboutClick() },
        color = ReviewRed,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ReviewPoint",
                color = TextOnRed,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun QuoteSection(quote: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)), // Increased alpha for better contrast
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = ReviewRed,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = quote,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp
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
        color = TextPrimary
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
        placeholder = { Text("Search reviewers...", color = TextMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledContainerColor = Color.White,
            disabledBorderColor = Color.LightGray,
            disabledLeadingIconColor = TextSecondary,
            disabledPlaceholderColor = TextMuted
        )
    )
}
