package com.devpair.offline.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpair.offline.data.repository.UserRepository
import com.devpair.offline.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val currentUser: User? = null,
    val allUsers: List<User> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Load current user
            userRepository.observeUser(UserRepository.CURRENT_USER_ID).collect { user ->
                _uiState.update { it.copy(currentUser = user, isLoading = false) }
            }
        }

        viewModelScope.launch {
            // Load all users
            userRepository.observeAllUsers().collect { users ->
                _uiState.update { it.copy(allUsers = users) }
            }
        }
    }
}
