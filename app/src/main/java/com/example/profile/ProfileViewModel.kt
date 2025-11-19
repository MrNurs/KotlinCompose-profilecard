package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            val local = repo.getUser(id)

            if (local != null) {
                _user.value = local
            } else {
                runCatching { repo.refreshUser(id) }
                _user.value = repo.getUser(id)
            }
        }
    }

    fun saveChanges(
        id: String,
        name: String,
        bio: String
    ) {
        viewModelScope.launch {
            val current = _user.value
            val updated = User(
                id = id,
                name = name,
                bio = bio,
                followers = current?.followers ?: mutableListOf()
            )
            repo.upsert(updated)
            _user.value = updated
        }
    }
    fun refreshFromRemote(id: String) {
        viewModelScope.launch {
            repo.refreshUser(id)
            _user.value = repo.getUser(id)
        }
    }
}
