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
import androidx.room.Room
import androidx.lifecycle.lifecycleScope
import com.example.profile.data.UserRepository
import com.example.profile.data.local.AppDatabase
import com.example.profile.data.local.UserEntity
import com.example.profile.ui.theme.ProfileTheme
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object MyProfile : Screen("myscreen")

    data object Profile : Screen("profile/{userId}") {
        const val ARG = "userId"
        fun path(userId: String) = "profile/$userId"
    }

    data object EditProfile : Screen("profile/{userid}/edit") {
        const val ARG = "userid"
        fun path(userId: String) = "profile/$userId/edit"
    }
}

class MainActivity : ComponentActivity() {

    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app.db"
        ).build()

        repo = UserRepository(db.userDao())

        lifecycleScope.launch {
            if (repo.getUser("me") == null) {
                repo.upsert(
                    UserEntity(
                        id = "me",
                        name = "Dilmagambet Nurzhigit",
                        bio = "Computer Science | BACKEND/ROBOTICS"
                    )
                )
            }
        }

        setContent {
            ProfileTheme {
                AppNav(repo)
            }
        }
    }
}

@Composable
fun AppNav(repo: UserRepository) {
    val myID = "me"
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(onOpenProfile = { nav.navigate(Screen.MyProfile.route) })
        }

        composable(Screen.MyProfile.route) {
            ProfileScreen(
                id = myID,
                repo = repo,
                onEdit = { userId -> nav.navigate(Screen.EditProfile.path(userId)) }
            )
        }

        composable(
            Screen.EditProfile.route,
            arguments = listOf(
                navArgument(Screen.EditProfile.ARG) { type = NavType.StringType }
            )
        ) { backStack ->
            val id = backStack.arguments?.getString(Screen.EditProfile.ARG)!!
            EditProfile(
                id = id,
                repo = repo,
                backToProfile = { nav.navigateUp() }
            )
        }
    }
}
