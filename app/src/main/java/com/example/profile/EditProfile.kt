package com.example.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@Composable
fun EditProfile(id: String, modifier: Modifier = Modifier, backToProfile: (String) -> Unit = {} ) {
    var user = UserRepository.getUser(id)
    var name by remember { mutableStateOf(TextFieldValue(user?.name ?: "")) }
    var bio by remember { mutableStateOf(TextFieldValue(user?.bio ?: "")) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Edit Profile", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (user != null) {
                    UserRepository.updateUser(
                        user.copy(name = name.text, bio = bio.text)
                    )
                }
                backToProfile(id)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}
