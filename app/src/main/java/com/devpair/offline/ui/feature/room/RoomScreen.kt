package com.devpair.offline.ui.feature.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.hilt.navigation.compose.hiltViewModel
import com.devpair.offline.R
import com.devpair.offline.data.repository.UserRepository
import com.devpair.offline.ui.components.MessageRow
import com.devpair.offline.ui.components.SessionInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    onNavigateBack: () -> Unit,
    viewModel: RoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-scroll to bottom quando arrivano nuovi messaggi
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.room_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics { contentDescription = "Back button" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .semantics { contentDescription = "Message input area" },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.messageInput,
                        onValueChange = { viewModel.updateMessageInput(it) },
                        placeholder = { Text(stringResource(R.string.type_message)) },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSending,
                        singleLine = false,
                        maxLines = 3
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = { viewModel.sendMessage() },
                        enabled = !uiState.isSending && uiState.messageInput.isNotBlank(),
                        modifier = Modifier.semantics { contentDescription = "Send message button" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = stringResource(R.string.send),
                            tint = if (uiState.messageInput.isNotBlank()) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Session info
            uiState.session?.let { session ->
                SessionInfo(session = session)
            }

            // Messages list
            if (uiState.messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No messages yet. Start the conversation!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                        .semantics { contentDescription = "Messages list" }
                ) {
                    items(
                        items = uiState.messages,
                        key = { it.id }
                    ) { message ->
                        val sender = uiState.users[message.senderId]
                        val senderName = sender?.displayName ?: "Unknown"
                        val isCurrentUser = message.senderId == UserRepository.CURRENT_USER_ID

                        MessageRow(
                            message = message,
                            senderName = senderName,
                            isCurrentUser = isCurrentUser
                        )
                    }
                }
            }
        }
    }
}
