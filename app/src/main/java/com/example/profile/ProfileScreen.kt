package com.example.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.animateColorAsState
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.profile.ui.theme.ProfileTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab

data class Follower(val name: String, val isFollowing: Boolean)

data class Post(
    val id: Int,
    val author: String,
    val text: String,
    val likes: Int = 0,
    val comments: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    id: String,
    modifier: Modifier = Modifier,
    onEdit: (String) -> Unit = {},
    viewModel: ProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val user by viewModel.user.collectAsState()

    LaunchedEffect(id) {
        viewModel.load(id)
    }

    val safeUser = user ?: User(
        id = "null",
        name = "unknown",
        bio = "",
        followers = mutableListOf("peter", "gosling")
    )
    val name = safeUser.name

    // --- Tabs ---
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    // --- Feed posts ---
    val posts = remember {
        mutableStateListOf(
            Post(1, name, "Good", likes = 5, comments = 2),
            Post(2, name, "Bad", likes = 8, comments = 3),
            Post(3, "Peter Parker", "I forgive you", likes = 12, comments = 4)
        )
    }

    // --- Follow / followers ---
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followText by rememberSaveable { mutableStateOf("Follow") }
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
            Follower("Rayan Gosling", true),
            Follower("Iam Legend", true),
            Follower("Peter Parker", true),
            Follower("570tenge", true),
            Follower("Daniyar Daraboz", true),
            Follower("Megaknight", true),
            Follower("CJ", true),
            Follower("Trevor", true),
            Follower("Khan", true),
            Follower("John Pork", true),
        )
    }

    val factory = CounterViewModelFactory(followers.size)
    val countVM: CounterViewModel = viewModel(factory = factory)

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

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ---------- TAB ROW ----------
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF101010)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Profile") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Feed") }
                )
            }

            // ---------- TAB CONTENT ----------
            when (selectedTab) {
                0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
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
                                    text = "subscribers: ${countVM.count}",
                                    fontSize = 15.sp,
                                    color = Color(150, 150, 150)
                                )

                                Text(
                                    safeUser.bio,
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
                                                countVM.increment()
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

                                    OutlinedButton(onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Messaging $name…")
                                        }
                                    }) {
                                        Text("Message", color = White)
                                    }
                                }

                                OutlinedButton(onClick = { onEdit(safeUser.id) }) {
                                    Text("Edit", color = Color.White)
                                }

                                Spacer(Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Followers", color = White, fontSize = 18.sp)
                                    IconButton(onClick = { followersExpanded = !followersExpanded }) { }
                                }

                                AnimatedVisibility(
                                    visible = followersExpanded,
                                    enter = expandVertically(),
                                    exit = shrinkVertically()
                                ) {

                                    val listState = rememberLazyListState()

                                    LazyColumn(
                                        state = listState,
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(320.dp)
                                            .padding(horizontal = 12.dp)
                                    ) {
                                        items(followers, key = { it.name + it.isFollowing }) { follower ->
                                            FollowerRow(
                                                follower = follower,
                                                onUnfollow = {
                                                    followers.remove(follower)
                                                    countVM.decrement()
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Removed ${follower.name}")
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }
                }
                1 -> {
                    FeedTabContent(posts = posts)
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
                        countVM.decrement()
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

// ---------------- VIEWMODELS / HELPERS -----------------

class CounterViewModel(private val number: Int) : ViewModel() {
    var count by mutableStateOf(number)
        private set

    fun increment() { count++ }
    fun decrement() { count-- }
}

class CounterViewModelFactory(private val initialCount: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterViewModel(initialCount) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun FollowerRow(
    follower: Follower,
    onUnfollow: () -> Unit
) {
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
            Text(
                text = follower.name,
                color = Color.White,
                fontSize = 16.sp
            )
            TextButton(onClick = onUnfollow) {
                Text("Remove", color = Color(0xFFBBBBBB))
            }
        }
    }
}

// ------------------ FEED TAB ------------------

@Composable
fun FeedTabContent(
    posts: List<Post>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181818)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(post.author, color = Color.White, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(post.text, color = Color(0xFFDDDDDD), fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))

                    var likes by remember { mutableStateOf(post.likes) }
                    var comments by remember { mutableStateOf(post.comments) }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { likes++ }) {
                            Text("+ $likes", color = Color.White)
                        }
                        TextButton(onClick = { comments++ }) {
                            Text("- $comments", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
