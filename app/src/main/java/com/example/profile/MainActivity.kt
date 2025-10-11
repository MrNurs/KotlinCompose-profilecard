package com.example.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

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
import kotlinx.coroutines.launch

data class Follower(val name: String, val isFollowing: Boolean)

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
    var followersExpanded by rememberSaveable { mutableStateOf(true) }

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

    val followers = remember {
        mutableStateListOf(
            Follower("Rayan Gosling", false),
            Follower("Iam Legend", true),
            Follower("Peter Parker", false),
            Follower("570tenge", true),
            Follower("Daniyar Daraboz", false),
            Follower("Megaknight", false),
            Follower("CJ", true),
            Follower("Trevor", false),
            Follower("Khan", true),
            Follower("John Pork", false),
        )
    }


    LaunchedEffect(isFollowing) {
        followText = if (isFollowing) "Unfollow" else "Follow"
    }

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
                        .width(350.dp)
                        .background(Color(red = 10, green = 10, blue = 10)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(24.dp))

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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (!isFollowing) {
                                    isFollowing = true
                                    count = (count + 1).coerceAtLeast(0)
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

                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Messaging $name…")
                                }
                            }
                        ) {
                            Text("Message", color = White)
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Followers", color = White, fontSize = 18.sp)
                        IconButton(onClick = { followersExpanded = !followersExpanded }) {

                        }
                    }

                    AnimatedVisibility(
                        visible = followersExpanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {

                        val listState = rememberLazyListState()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            LazyColumn(
                                state = listState,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(followers, key = { it.name + it.isFollowing }) { follower ->
                                    FollowerRow(
                                        follower = follower,
                                        onUnfollow = {
                                            followers.remove(follower)
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Removed ${follower.name}")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
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
                    if (isFollowing) {
                        isFollowing = false
                        count = (count - 1).coerceAtLeast(0)
                    }
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

@Composable
fun FollowerRow(follower: Follower, onUnfollow: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181818)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(follower.name, color = Color.White, fontSize = 16.sp)
            TextButton(onClick = onUnfollow) {
                Text("Remove", color = Color(0xFFBBBBBB))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfileTheme {
        Greeting("Dilmagambet Nurzhigit")
    }
}
