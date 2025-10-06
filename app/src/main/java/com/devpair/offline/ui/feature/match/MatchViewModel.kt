package com.devpair.offline.ui.feature.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpair.offline.data.repository.SessionRepository
import com.devpair.offline.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchUiState(
    val language: String = "Kotlin",
    val durationMinutes: Int = 30,
    val isCreating: Boolean = false
)

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchUiState())
    val uiState: StateFlow<MatchUiState> = _uiState.asStateFlow()

    fun updateLanguage(language: String) {
        _uiState.value = _uiState.value.copy(language = language)
    }

    fun updateDuration(minutes: Int) {
        _uiState.value = _uiState.value.copy(durationMinutes = minutes)
    }

    fun createSession(onSessionCreated: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true)
            
            val sessionId = sessionRepository.createSession(
                hostId = UserRepository.CURRENT_USER_ID,
                language = _uiState.value.language,
                durationMinutes = _uiState.value.durationMinutes
            )
            
            _uiState.value = _uiState.value.copy(isCreating = false)
            onSessionCreated(sessionId)
        }
    }
}
