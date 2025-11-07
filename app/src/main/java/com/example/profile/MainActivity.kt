package com.example.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.profile.ui.theme.ProfileTheme

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    // My profile
    data object MyProfile : Screen("myscreen"){}

    // futureStuff
    data object Profile : Screen("profile/{userId}") {
        const val ARG = "userId"
        fun path(userId: String) = "profile/$userId" // build path
    }
    // Edit profile
    data object EditProfile : Screen("profile/{userid}/edit") {
        const val ARG = "userid"
        fun path(userId: String) = "profile/$userId/edit"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileTheme { AppNav() }
        }
    }
}

@Composable
fun AppNav() {
    val myID = "me"
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenProfile = { nav.navigate(Screen.MyProfile.route) }
            )
        }

        composable(Screen.MyProfile.route) {

            ProfileScreen(
                id = myID,
                onEdit = { userId -> nav.navigate(Screen.EditProfile.path(userId)) }
            )
        }

        composable(
            route = Screen.EditProfile.route,
            arguments = listOf(navArgument(Screen.EditProfile.ARG) { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString(Screen.EditProfile.ARG)!!
            EditProfile(
                id = id,
                backToProfile = {
                    nav.navigateUp()
                }
            )
        }

    }
}
