package com.example.app.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app.ui.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Upload : Screen("upload")
    object Overview : Screen("overview")
    object Profile : Screen("profile")
    object About : Screen("about")
    object LatestReviewers : Screen("latest_reviewers")
    object SubjectDetail : Screen("subject_detail?subjectName={subjectName}") {
        fun createRoute(subjectName: String) = "subject_detail?subjectName=${Uri.encode(subjectName)}"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onSubjectClick = { subject ->
                    navController.navigate(Screen.SubjectDetail.createRoute(subject))
                },
                onViewLatestReviewers = { navController.navigate(Screen.LatestReviewers.route) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onSearchClick = { navController.navigate(Screen.Search.route) }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                onSubjectClick = { subject ->
                    navController.navigate(Screen.SubjectDetail.createRoute(subject))
                }
            )
        }
        composable(Screen.Upload.route) {
            UploadScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Overview.route) {
            OverviewScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.LatestReviewers.route) {
            LatestReviewersScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.SubjectDetail.route,
            arguments = listOf(
                navArgument("subjectName") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
            SubjectDetailScreen(
                subjectName = subjectName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
