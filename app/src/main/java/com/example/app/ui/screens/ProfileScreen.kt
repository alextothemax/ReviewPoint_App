package com.example.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.ReviewerRepository
import com.example.app.data.UserRepository
import com.example.app.model.Badge
import com.example.app.model.User
import com.example.app.ui.theme.ReviewRed
import com.example.app.ui.theme.ReviewYellow

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
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReviewRed)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Profile",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                UserInfoSection(currentUser)
            }
            item {
                AwardSystemSection(currentUser)
            }
        }
    }
}

@Composable
fun UserInfoSection(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(user.email, color = Color.Gray, fontSize = 14.sp)
                user.currentBadge?.let { badge ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = ReviewYellow.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            badge.title,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = ReviewYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            IconButton(onClick = { /* Edit profile */ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ReviewRed)
            }
        }
    }
}

@Composable
fun AwardSystemSection(user: User) {
    Column {
        Text("Ranking System", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
    val alpha = if (unlocked) 1f else 0.4f
    val ratingMet = user.averageRating >= badge.minRating
    val uploadsMet = user.uploadsCount >= badge.uploadsRequired
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) Color.White else Color.White.copy(alpha = 0.6f)
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
                    color = Color.Black.copy(alpha = alpha)
                )
                Text(
                    "${badge.uploadsRequired} Uploads${if(badge.minRating > 0) " & ${badge.minRating} Rating" else ""} required",
                    fontSize = 14.sp,
                    color = if (!uploadsMet || !ratingMet) Color.Red.copy(alpha = 0.6f) else Color.Gray.copy(alpha = alpha)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (unlocked) {
                Icon(Icons.Default.Star, contentDescription = "Unlocked", tint = ReviewYellow)
            }
        }
    }
}
