package com.devpair.offline.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devpair.offline.ui.feature.home.HomeScreen
import com.devpair.offline.ui.feature.match.MatchScreen
import com.devpair.offline.ui.feature.profile.ProfileScreen
import com.devpair.offline.ui.feature.room.RoomScreen

@Composable
fun DevPairNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToMatch = {
                    navController.navigate(Screen.Match.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Match.route) {
            MatchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSessionCreated = { sessionId ->
                    navController.navigate(Screen.Room.createRoute(sessionId)) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(
            route = Screen.Room.route,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.StringType }
            )
        ) {
            RoomScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
