package com.example.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.profile.data.UserRepository
import com.example.profile.data.local.UserEntity
import kotlinx.coroutines.launch

@Composable
fun EditProfile(
    id: String,
    repo: UserRepository,
    backToProfile: (String) -> Unit
) {
    var user by remember { mutableStateOf<UserEntity?>(null) }

    LaunchedEffect(id) {
        user = repo.getUser(id)
    }

    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            bio = it.bio
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )

        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                scope.launch {
                    repo.upsert(
                        UserEntity(
                            id = id,
                            name = name,
                            bio = bio
                        )
                    )
                    backToProfile(id)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

    }
}
