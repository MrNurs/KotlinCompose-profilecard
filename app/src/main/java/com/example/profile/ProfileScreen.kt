package com.example.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale


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
    viewModel: ProfileViewModel = hiltViewModel()
) {

    var recompositionCount by remember { mutableStateOf(0) }
    SideEffect { recompositionCount++ }

    val user by viewModel.user.collectAsState()

    LaunchedEffect(id) {
        viewModel.load(id)
    }
    // Shimmer while loading
    if (user == null) {
        ProfileShimmer()
        return
    }

    val safeUser = user!!
    val name = safeUser.name

    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followText by rememberSaveable { mutableStateOf("Follow") }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }
    var followersExpanded by rememberSaveable { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Posts
    val posts = remember {
        mutableStateListOf(
            Post(1, name, "Good", likes = 5, comments = 2),
            Post(2, name, "Bad", likes = 8, comments = 3),
            Post(3, "Peter Parker", "I forgive you", likes = 12, comments = 4)
        )
    }

    // Followers
    val followers = remember {
        mutableStateListOf(
            Follower("Rayan Gosling", true),
            Follower("Peter Parker", true),
            Follower("570tenge", true),
            Follower("Daniyar Daraboz", true),
            Follower("CJ", true),
            Follower("Megaknight", true),
        )
    }
    // Counter VM
    val factory = CounterViewModelFactory(followers.size)
    val countVM: CounterViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

    val subscribersText by remember {
        derivedStateOf { "subscribers: ${countVM.count}" }
    }

    LaunchedEffect(isFollowing) {
        followText = if (isFollowing) "Unfollow" else "Follow"
    }

    var statsVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { statsVisible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Screen", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1A1A1A))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF0C0C0C)
    ) { pad ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(pad)
        ) {

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

//            Text(
//                text = "Recompositions: $recompositionCount",
//                color = Color.Gray,
//                fontSize = 12.sp,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(top = 4.dp)
//            )

            when (selectedTab) {

                // PROFILE TAB
                0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.width(350.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(Color(0xFF101010)),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0A0A0A)),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Spacer(Modifier.height(24.dp))

                                AvatarWithStatus(isFollowing = isFollowing)

                                Spacer(Modifier.height(12.dp))

                                Text(name, fontSize = 22.sp, color = Color.White)

                                AnimatedVisibility(
                                    visible = statsVisible,
                                    enter = fadeIn(tween(700))
                                ) {
                                    Text(
                                        subscribersText,
                                        fontSize = 15.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                Text(
                                    safeUser.bio,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                                )

                                // FOLLOW BUTTONS
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
                                            } else showUnfollowDialog = true
                                        }
                                    ) { Text(followText) }

                                    OutlinedButton(onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Messaging $name…")
                                        }
                                    }) {
                                        Text("Message", color = Color.White)
                                    }
                                }

                                OutlinedButton(onClick = { onEdit(safeUser.id) }) {
                                    Text("Edit", color = Color.White)
                                }

                                Spacer(Modifier.height(20.dp))

                                // FOLLOWERS
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Followers", color = Color.White, fontSize = 18.sp)
                                    IconButton(onClick = { followersExpanded = !followersExpanded }) {}
                                }

                                AnimatedVisibility(
                                    visible = followersExpanded,
                                    enter = expandVertically(),
                                    exit = shrinkVertically()
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .height(300.dp)
                                            .padding(horizontal = 12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(followers, key = { it.name }) { follower ->
                                            AnimatedFollowerRow(
                                                follower = follower,
                                                onRemove = {
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

                // FEED TAB
                1 -> FeedTabContentAnimated(posts)
            }
        }
    }

    // CONFIRM DIALOG
    if (showUnfollowDialog) {
        AlertDialog(
            onDismissRequest = { showUnfollowDialog = false },
            title = { Text("Unfollow $name?") },
            confirmButton = {
                TextButton(onClick = {
                    isFollowing = false
                    countVM.decrement()
                    showUnfollowDialog = false
                }) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = { showUnfollowDialog = false }) { Text("Cancel") }
            },
            containerColor = Color(0xFF1E1E1E)
        )
    }
}

// SHIMMER

@Composable
fun ProfileShimmer() {
    val shimmer = rememberInfiniteTransition()
    val alpha by shimmer.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            tween(600),
            RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha))
        )
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .height(20.dp)
                .width(120.dp)
                .background(Color.Gray.copy(alpha), RoundedCornerShape(8.dp))
        )
    }
}



@Composable
fun AnimatedFollowerRow(
    follower: Follower,
    onRemove: () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        finishedListener = {
            if (!isVisible) onRemove()
        }
    )

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.7f,
        animationSpec = tween(500)
    )

    val offset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 50.dp,
        animationSpec = tween(500)
    )

    if (alpha <= 0f) return

    FollowerRow(
        follower = follower,
        onUnfollow = {
            isVisible = false
        },
        modifier = Modifier
            .graphicsLayer(
                alpha = alpha,
                scaleX = scale,
                scaleY = scale
            )
            .offset(x = offset)
    )
}

// FOLLOWER ROW
@Composable
fun FollowerRow(
    follower: Follower,
    onUnfollow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181818)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = follower.name,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onUnfollow,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Remove", color = Color(0xFFBBBBBB))
            }
        }
    }
}


// FEED WITH SLIDE ANIMATION
@Composable
fun FeedTabContentAnimated(posts: List<Post>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts, key = { it.id }) { post ->

            var startOffset by remember { mutableStateOf(true) }
            LaunchedEffect(Unit) { startOffset = false }

            val offsetX by animateDpAsState(
                targetValue = if (startOffset) 200.dp else 0.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color(0xFF181818))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(post.author, color = Color.White, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(post.text, color = Color.LightGray, fontSize = 14.sp)

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
// VIEWMODELS / HELPERS

class CounterViewModel(private val number: Int) : ViewModel() {
    var count by mutableStateOf(number)
        private set

    fun increment() { count++ }
    fun decrement() { count-- }
}

@Composable
private fun AvatarWithStatus(
    isFollowing: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data("https://media.desenio.com/site_images/68631e5292c536b9cc92b07c_1776830038_WB0125-5.jpg?auto=compress%2Cformat&fit=max&w=3840")
            .crossfade(true)
            .build()
    }

    val avatarScale by animateFloatAsState(
        targetValue = if (isFollowing) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.25f,
            stiffness = 50f
        ),
        label = "avatarScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "onlinePulseTransition")
    val onlinePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "onlinePulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AsyncImage(
            model = imageRequest,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.literallyme),
            error = painterResource(id = R.drawable.literallyme),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(96.dp)
                .graphicsLayer(
                    scaleX = avatarScale,
                    scaleY = avatarScale
                )
                .clip(CircleShape)
        )

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .size(14.dp)
                .graphicsLayer(
                    scaleX = onlinePulse,
                    scaleY = onlinePulse
                )
                .background(Color(0xFF4CAF50), CircleShape)
        )
    }
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
