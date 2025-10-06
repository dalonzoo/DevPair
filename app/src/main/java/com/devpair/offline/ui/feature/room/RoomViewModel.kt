package com.devpair.offline.ui.feature.room

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpair.offline.data.repository.MessageRepository
import com.devpair.offline.data.repository.SessionRepository
import com.devpair.offline.data.repository.UserRepository
import com.devpair.offline.domain.model.Message
import com.devpair.offline.domain.model.Session
import com.devpair.offline.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomUiState(
    val session: Session? = null,
    val messages: List<Message> = emptyList(),
    val users: Map<String, User> = emptyMap(),
    val messageInput: String = "",
    val isSending: Boolean = false
)

@HiltViewModel
class RoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val sessionId: String = checkNotNull(savedStateHandle["sessionId"])

    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
        // Observe session
        viewModelScope.launch {
            sessionRepository.observeSession(sessionId).collect { session ->
                _uiState.update { it.copy(session = session) }
            }
        }

        // Observe messages
        viewModelScope.launch {
            messageRepository.observeMessages(sessionId).collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }

        // Load users
        viewModelScope.launch {
            userRepository.observeAllUsers().collect { userList ->
                val userMap = userList.associateBy { it.id }
                _uiState.update { it.copy(users = userMap) }
            }
        }
    }

    fun updateMessageInput(text: String) {
        _uiState.value = _uiState.value.copy(messageInput = text)
    }

    fun sendMessage() {
        val text = _uiState.value.messageInput.trim()
        if (text.isBlank() || _uiState.value.isSending) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)
            
            messageRepository.sendMessage(
                sessionId = sessionId,
                senderId = UserRepository.CURRENT_USER_ID,
                text = text
            )
            
            _uiState.value = _uiState.value.copy(
                messageInput = "",
                isSending = false
            )
        }
    }
}
