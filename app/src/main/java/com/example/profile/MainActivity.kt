package com.example.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.profile.ui.theme.ProfileTheme
import androidx.compose.animation.animateColorAsState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileTheme {
                Greeting(name = "Dilmagambet Nurzhigit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followText by rememberSaveable { mutableStateOf("Follow") }
    var count by rememberSaveable { mutableStateOf(0) }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) Color.Black else Color.White,
        label = "buttonColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isFollowing) Color.White else Color.Black,
        label = "contentColor"
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Screen", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF0C0C0C)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(red = 12, green = 12, blue = 12)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(350.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF101010))
            ) {
                Column(
                    modifier = Modifier
                        .height(800.dp)
                        .width(350.dp)
                        .background(Color(red = 10, green = 10, blue = 10)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.literallyme),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(name, fontSize = 22.sp, color = White)
                    Text(
                        text = "subscribers: $count",
                        fontSize = 15.sp,
                        color = Color(red = 150, green = 150, blue = 150)
                    )
                    Text(
                        "Computer Science | BACKEND/ROBOTICS",
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                        color = White
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                if (!isFollowing) {
                                    isFollowing = true
                                    followText = "UnFollow"
                                    count++
                                    scope.launch {
                                        snackbarHostState.showSnackbar("You followed $name!")
                                    }
                                } else {
                                    showUnfollowDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                                contentColor = contentColor
                            )
                        ) {
                            Text(followText)
                        }

                        OutlinedButton(onClick = {}) {
                            Text("Message", color = White)
                        }
                    }
                }
            }
        }
    }

    if (showUnfollowDialog) {
        AlertDialog(
            onDismissRequest = { showUnfollowDialog = false },
            title = { Text("Unfollow $name?") },
            text = { Text("Are you sure you want to unfollow this user?") },
            confirmButton = {
                TextButton(onClick = {
                    isFollowing = false
                    followText = "Follow"
                    count = maxOf(0, count - 1)
                    showUnfollowDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnfollowDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = Color(0xFF1E1E1E),
            titleContentColor = Color.White,
            textContentColor = Color.LightGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfileTheme {
        Greeting("Dilmagambet Nurzhigit")
    }
}
