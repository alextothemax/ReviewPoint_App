package com.example.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.ReviewerRepository
import com.example.app.data.UserRepository
import com.example.app.model.Reviewer
import com.example.app.ui.components.allAvailableSubjects
import com.example.app.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(onBack: () -> Unit = {}) {
    var subject by remember { mutableStateOf(allAvailableSubjects.firstOrNull()?.name ?: "") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    
    // Store selected URI and name
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedFileUri = uri
            selectedFileName = uri.lastPathSegment ?: "Selected File"
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by UserRepository.currentUser
    val scrollState = rememberScrollState()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        focusedLabelColor = ReviewRed,
        unfocusedLabelColor = TextSecondary,
        focusedBorderColor = ReviewRed,
        unfocusedBorderColor = Color.LightGray,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedPlaceholderColor = TextMuted,
        unfocusedPlaceholderColor = TextMuted
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
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
                    text = "Upload Reviewer",
                    color = TextOnRed,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundWhite)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Subject Dropdown
            Text("Subject:", fontWeight = FontWeight.Bold, color = TextPrimary)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    allAvailableSubjects.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.name, color = TextPrimary) },
                            onClick = {
                                subject = selectionOption.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Title
            Text("Reviewer Title:", fontWeight = FontWeight.Bold, color = TextPrimary)
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Basics of Organic Chemistry") },
                shape = RoundedCornerShape(8.dp),
                colors = textFieldColors,
                singleLine = true
            )

            // Upload File Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Upload File:", fontWeight = FontWeight.Bold, color = TextPrimary)
                TextButton(onClick = { 
                    filePickerLauncher.launch(arrayOf(
                        "application/pdf", 
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // docx
                        "application/vnd.ms-powerpoint", // ppt
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation" // pptx
                    )) 
                }) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp), tint = ReviewRed)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (selectedFileName == null) "Select File" else "Change File", color = ReviewRed, fontWeight = FontWeight.Bold)
                }
            }
            
            if (selectedFileName != null) {
                FileItem(
                    fileName = selectedFileName!!,
                    iconColor = Color(0xFFC62828),
                    icon = Icons.Default.Description,
                    onDelete = {
                        selectedFileUri = null
                        selectedFileName = null
                    }
                )
            } else {
                Text(
                    "No file selected",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Description
            Text("Description:", fontWeight = FontWeight.Bold, color = TextPrimary)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = { Text("Enter a brief description of the reviewer...") },
                shape = RoundedCornerShape(8.dp),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Upload Button
            Button(
                onClick = {
                    if (title.isNotBlank() && subject.isNotBlank() && selectedFileUri != null) {
                        // 1. Copy file to internal storage
                        val localName = ReviewerRepository.saveFileToInternal(context, selectedFileUri!!)
                        
                        if (localName != null) {
                            // 2. Add to repository
                            val newReviewer = Reviewer(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                subject = subject,
                                uploaderName = currentUser?.name ?: "Guest",
                                uploadTimeAgo = "Just now",
                                downloads = 0,
                                views = 0,
                                rating = 0.0,
                                timestamp = System.currentTimeMillis(),
                                localFileName = localName
                            )
                            ReviewerRepository.addReviewer(newReviewer)
                            onBack()
                        } else {
                            // Show error
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ReviewRed),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && selectedFileUri != null
            ) {
                Text("Upload", color = TextOnRed, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun FileItem(
    fileName: String,
    iconColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onDelete: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconColor)
        Spacer(modifier = Modifier.width(12.dp))
        Text(fileName, fontSize = 14.sp, color = TextPrimary, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}
