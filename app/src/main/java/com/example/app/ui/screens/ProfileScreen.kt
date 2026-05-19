package com.example.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.ReviewerRepository
import com.example.app.data.UserRepository
import com.example.app.model.Badge
import com.example.app.model.User
import com.example.app.ui.theme.*

@Composable
fun ProfileScreen() {
    val userState by UserRepository.currentUser
    val myReviewers = remember(userState) {
        ReviewerRepository.reviewers.filter { it.uploaderName == userState?.name }
    }
    
    val uploadsCount = myReviewers.size
    val averageRating = if (myReviewers.isNotEmpty()) {
        myReviewers.sumOf { it.rating } / myReviewers.size
    } else 0.0

    val currentUser = User(
        id = "1",
        name = userState?.name ?: "Guest",
        email = userState?.email ?: "guest@example.com",
        uploadsCount = uploadsCount,
        averageRating = averageRating
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // Responsive Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReviewRed)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextOnRed)
            }
            Text(
                text = "Profile",
                color = TextOnRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "ReviewPoint",
                color = TextOnRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                UserInfoSection(
                    user = currentUser,
                    onLogout = { UserRepository.logout() }
                )
            }
            item {
                AwardSystemSection(currentUser)
            }
        }
    }
}

@Composable
fun UserInfoSection(user: User, onLogout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.White)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 20.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.email, 
                    color = TextSecondary, 
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                user.currentBadge?.let { badge ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = ReviewYellow.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            badge.title,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = ReviewYellow, // Yellow usually has enough contrast on white if it's dark enough, but maybe need a darker yellow
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            TextButton(
                onClick = onLogout,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text("Log out", color = ReviewRed, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AwardSystemSection(user: User) {
    Column {
        Text("Ranking System", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        
        Badge.entries.forEach { badge ->
            val unlocked = user.uploadsCount >= badge.uploadsRequired && user.averageRating >= badge.minRating
            BadgeItem(badge, unlocked, user)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun BadgeItem(badge: Badge, unlocked: Boolean, user: User) {
    val ratingMet = user.averageRating >= badge.minRating
    val uploadsMet = user.uploadsCount >= badge.uploadsRequired
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) Color.White else Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (unlocked) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = if (unlocked) ReviewYellow else Color.LightGray,
                shadowElevation = if (unlocked) 2.dp else 0.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    badge.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (unlocked) TextPrimary else TextMuted
                )
                Text(
                    "${badge.uploadsRequired} Uploads${if(badge.minRating > 0) " & ${badge.minRating} Rating" else ""} required",
                    fontSize = 14.sp,
                    color = if (!uploadsMet || !ratingMet) ReviewRed else TextSecondary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (unlocked) {
                Icon(Icons.Default.Star, contentDescription = "Unlocked", tint = ReviewYellow)
            }
        }
    }
}
