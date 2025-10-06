package com.devpair.offline.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Match : Screen("match")
    data object Room : Screen("room/{sessionId}") {
        fun createRoute(sessionId: String) = "room/$sessionId"
    }
    data object Profile : Screen("profile")
}
