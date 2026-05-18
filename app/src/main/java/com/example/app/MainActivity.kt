package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app.data.UserRepository
import com.example.app.ui.navigation.NavGraph
import com.example.app.ui.navigation.Screen
import com.example.app.ui.screens.LoginScreen
import com.example.app.ui.splash.SplashScreen
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.ReviewYellow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                var showSplash by remember { mutableStateOf(true) }
                val currentUser by UserRepository.currentUser

                if (showSplash) {
                    SplashScreen(onAnimationFinished = { showSplash = false })
                } else if (currentUser == null) {
                    LoginScreen(onLoginSuccess = { /* State will update and trigger recomposition */ })
                } else {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    NavigationItem("Home", Screen.Home.route, Icons.Default.Home),
                    NavigationItem("Search", Screen.Search.route, Icons.Default.Search),
                    NavigationItem("Upload", Screen.Upload.route, Icons.Default.Bookmark), // Placeholder for central yellow button
                    NavigationItem("Overview", Screen.Overview.route, Icons.AutoMirrored.Filled.LibraryBooks),
                    NavigationItem("Profile", Screen.Profile.route, Icons.Default.Person)
                )

                items.forEach { item ->
                    val isHomeTab = item.route == Screen.Home.route && 
                        (currentDestination?.route == Screen.LatestReviewers.route ||
                         currentDestination?.route == Screen.About.route ||
                         currentDestination?.route?.startsWith("subject_detail") == true)
                    
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true || isHomeTab
                    
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            val currentRoute = currentDestination?.route
                            if (item.route == Screen.Home.route && isHomeTab && currentRoute != Screen.Home.route) {
                                // If we are in a sub-page of Home (like Detail) and click Home, go back to root
                                navController.popBackStack(Screen.Home.route, inclusive = false)
                            } else if (currentRoute != item.route) {
                                // Standard navigation to a different tab
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            if (item.route == Screen.Upload.route) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(ReviewYellow, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        tint = Color.White
                                    )
                                }
                            } else {
                                Icon(item.icon, contentDescription = item.label)
                            }
                        },
                        label = { Text(item.label) },
                        colors = if (item.route == Screen.Upload.route) {
                            NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        } else {
                            NavigationBarItemDefaults.colors()
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController)
        }
    }
}

data class NavigationItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen()
    }
}
