package com.example.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EditProfile(
    id: String,
    viewModel: ProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    backToProfile: () -> Unit
) {
    val user by viewModel.user.collectAsState()

    LaunchedEffect(id) {
        viewModel.load(id)
    }

    var name by remember(user) { mutableStateOf(user?.name ?: "") }
    var bio by remember(user) { mutableStateOf(user?.bio ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edit profile", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.saveChanges(
                    id = id,
                    name = name,
                    bio = bio
                )
                backToProfile()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

    }
}
