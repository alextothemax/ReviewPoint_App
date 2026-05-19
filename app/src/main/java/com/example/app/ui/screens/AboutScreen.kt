package com.example.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.*

@Composable
fun AboutScreen(onBack: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReviewRed)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextOnRed)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "About ReviewPoint",
                color = TextOnRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000)) + scaleIn(),
            ) {
                // Main Logo Box
                Surface(
                    modifier = Modifier
                        .size(160.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "ReviewPoint Logo",
                            tint = Color(0xFF1A237E),
                            modifier = Modifier.size(100.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 20.dp)
                                .size(24.dp)
                                .background(ReviewYellow, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // About Section
            InfoCard(
                visible = visible,
                delay = 300,
                title = "About ReviewPoint",
                content = "ReviewPoint is a student-powered platform where you can share and access high-quality reviewers—helping you study smarter, save time, and succeed together.",
                icon = Icons.Default.School
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mission Section
            InfoCard(
                visible = visible,
                delay = 500,
                title = "Our Mission",
                content = "ReviewPoint empowers students to share and access high-quality reviewers in one organized platform—helping them study smarter, save time, and succeed together.",
                icon = Icons.AutoMirrored.Filled.Assignment
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Vision Section
            InfoCard(
                visible = visible,
                delay = 700,
                title = "Our Vision",
                content = "To become the leading student-driven platform for sharing knowledge, where learning is accessible, collaborative, and empowering for every student.",
                icon = Icons.Default.Visibility
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Developer Appreciation Section
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1500, delayMillis = 900)) + slideInVertically(initialOffsetY = { it / 2 }),
            ) {
                DeveloperSection()
            }
        }
    }
}

@Composable
fun InfoCard(
    visible: Boolean,
    delay: Int,
    title: String,
    content: String,
    icon: ImageVector
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1200, delayMillis = delay)) + expandVertically(),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ReviewRed,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ReviewRed
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = content,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun DeveloperSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = ReviewRed.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = Icons.Default.Badge,
                contentDescription = null,
                tint = ReviewRed,
                modifier = Modifier.padding(12.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Visionary Developer",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = 2.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Merlito V. Paulite Jr",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A237E),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        HorizontalDivider(
            modifier = Modifier.width(60.dp),
            thickness = 3.dp,
            color = ReviewYellow
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Empowering students through technology and collaboration.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
