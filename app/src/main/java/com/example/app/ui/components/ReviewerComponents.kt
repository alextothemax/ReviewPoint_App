package com.example.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.ReviewerRepository
import com.example.app.model.Badge
import com.example.app.model.Reviewer
import com.example.app.ui.theme.*

@Composable
fun TopSubjectsSection(
    onSubjectClick: (String) -> Unit = {},
    displaySubjects: List<Subject>
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Top Subjects", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(displaySubjects) { subject ->
                SubjectItem(subject, onClick = { onSubjectClick(subject.name) })
            }
        }
    }
}

@Composable
fun SubjectItem(subject: Subject, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Icon(
                imageVector = subject.icon,
                contentDescription = subject.name,
                modifier = Modifier.padding(16.dp),
                tint = TextSecondary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(subject.name, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
        if (actionText.isNotEmpty()) {
            Text(
                actionText,
                color = ReviewRed,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
fun LatestReviewersHeader(
    title: String = "Most Recent Reviewers",
    actionText: String = "See all",
    onViewAll: () -> Unit = {}
) {
    SectionHeader(
        title = title,
        actionText = actionText,
        onActionClick = onViewAll
    )
}

@Composable
fun ReviewerCard(reviewer: Reviewer) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE0F2F1)
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = Color(0xFF00796B)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reviewer.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = reviewer.subject,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.weight(1f, fill = false)) {
                        repeat(5) { index ->
                            val starRating = index + 1
                            Icon(
                                imageVector = if (starRating <= reviewer.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = ReviewYellow,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { ReviewerRepository.rateReviewer(reviewer.id, starRating.toDouble()) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${reviewer.downloads}",
                        fontSize = 12.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextPrimary)
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = ReviewYellow)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reviewer.uploaderName,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = " • ${reviewer.uploadTimeAgo}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }
            }
            
            IconButton(
                onClick = { ReviewerRepository.downloadReviewer(context, reviewer) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = ReviewRed
                )
            }
        }
    }
}

data class Subject(val name: String, val icon: ImageVector)

data class SubjectGroup(val programName: String, val subjects: List<String>)

val allAvailableSubjects = listOf(
    Subject("Mathematics", Icons.Default.Functions),
    Subject("English", Icons.AutoMirrored.Filled.MenuBook),
    Subject("Science", Icons.Default.Science),
    Subject("Social Studies / History", Icons.Default.AccountBalance),
    Subject("ICT / Computer", Icons.Default.Computer)
)

val groupedSubjects = listOf(
    SubjectGroup("JUNIOR HIGH SCHOOL", listOf("Filipino", "English", "Mathematics", "Science", "Araling Panlipunan", "MAPEH (Music, Arts, PE, Health)", "TLE (Technology & Livelihood Education)", "ESP (Edukasyon sa Pagpapakatao)")),
    SubjectGroup("STEM (Science, Technology, Engineering, Mathematics)", listOf("General Mathematics", "Pre-Calculus", "Basic Calculus", "Statistics & Probability", "Earth Science", "General Biology 1 & 2", "General Chemistry 1 & 2", "General Physics 1 & 2", "Practical Research 1 & 2")),
    SubjectGroup("ABM (Accountancy, Business, and Management)", listOf("Fundamentals of Accountancy, Business and Management (FABM) 1 & 2", "Business Mathematics", "Business Finance", "Organization and Management", "Principles of Marketing", "Applied Economics", "Entrepreneurship", "Business Ethics and Social Responsibility", "Practical Research 1 & 2")),
    SubjectGroup("HUMSS (Humanities and Social Sciences)", listOf("Introduction to World Religions and Belief Systems", "Disciplines and Ideas in Social Sciences", "Disciplines and Ideas in Applied Social Sciences", "Creative Writing", "Creative Nonfiction", "Philippine Politics and Governance", "Community Engagement, Solidarity and Citizenship", "Trends, Networks, and Critical Thinking", "Practical Research 1 & 2")),
    SubjectGroup("HE (Home Economics)", listOf("Cookery", "Bread and Pastry Production", "Food and Beverage Services", "Housekeeping", "Caregiving")),
    SubjectGroup("ICT (Information and Communications Technology)", listOf("Computer Systems Servicing (CSS)", "Programming", "Animation", "Technical Drafting")),
    SubjectGroup("IA (Industrial Arts)", listOf("Electrical Installation and Maintenance", "Automotive Servicing", "Welding", "Carpentry")),
    SubjectGroup("AFA (Agri-Fishery Arts)", listOf("Agricultural Crops Production", "Animal Production", "Aquaculture")),
    SubjectGroup("IT / COMPUTER SCIENCE", listOf("Programming (Java, Python, C++, etc.)", "Data Structures & Algorithms", "Database Management Systems", "Web Development", "Mobile App Development", "Software Engineering", "Networking", "Cybersecurity", "Operating Systems", "Human-Computer Interaction")),
    SubjectGroup("ENGINEERING", listOf("Engineering Mathematics", "Calculus (Differential, Integral)", "Physics for Engineers", "Chemistry for Engineers", "Thermodynamics", "Engineering Drawing", "Mechanics (Statics & Dynamics)", "Electronics", "Fluid Mechanics")),
    SubjectGroup("MEDICAL / HEALTH (Nursing, MedTech, etc.)", listOf("Anatomy", "Physiology", "Biochemistry", "Pharmacology", "Microbiology", "Nursing Fundamentals", "Medical-Surgical Nursing", "Community Health Nursing", "Medical Terminology")),
    SubjectGroup("BUSINESS / MANAGEMENT", listOf("Financial Accounting", "Managerial Accounting", "Marketing Management", "Financial Management", "Human Resource Management", "Operations Management", "Business Law", "Economics", "Entrepreneurship")),
    SubjectGroup("Psychology", listOf("General Psychology", "Developmental Psychology", "Abnormal Psychology", "Industrial Psychology")),
    SubjectGroup("Criminology", listOf("Criminal Law", "Law Enforcement", "Criminal Investigation", "Forensic Science")),
    SubjectGroup("Education", listOf("Teaching Methods", "Curriculum Development", "Assessment of Learning", "Educational Psychology")),
    SubjectGroup("Tourism / Hospitality", listOf("Tourism Management", "Hospitality Management", "Food & Beverage Services", "Front Office Operations"))
)

val sampleReviewers = listOf(
    Reviewer("1", "Calculus Basics", "Mathematics", "Jane Doe", "1 day ago", 500, 1200, 4.8, Badge.ACADEMIC_AUTHORITY),
    Reviewer("2", "Python Intro", "ICT / Computer", "Charlie Brown", "2 days ago", 600, 1500, 4.9, Badge.REVIEWPOINT_TITAN),
    Reviewer("3", "World History", "Social Studies / History", "Alice Lee", "10 hours ago", 450, 950, 4.6, Badge.KNOWLEDGE_CRAFTER),
    Reviewer("4", "English Grammar", "English", "Bob Wilson", "3 days ago", 300, 800, 4.2, Badge.FIRST_SPARK),
    Reviewer("5", "Chemistry Study Guide", "Science", "John Smith", "7 hours ago", 200, 500, 4.5, Badge.RISING_CONTRIBUTOR),
    Reviewer("6", "General Physics", "Science", "John Smith", "5 days ago", 150, 400, 4.0, Badge.KNOWLEDGE_CRAFTER)
)
