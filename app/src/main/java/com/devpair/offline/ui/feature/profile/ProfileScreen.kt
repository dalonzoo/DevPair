package com.devpair.offline.ui.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devpair.offline.R
import com.devpair.offline.domain.model.User
import com.devpair.offline.domain.model.UserLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_title)) },
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
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Current user
            item {
                Text(
                    text = "Your Profile",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            uiState.currentUser?.let { user ->
                item {
                    UserCard(
                        user = user,
                        isCurrentUser = true,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }

            // All users
            item {
                Text(
                    text = stringResource(R.string.all_users),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                )
            }

            items(uiState.allUsers) { user ->
                UserCard(
                    user = user,
                    isCurrentUser = false,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun UserCard(
    user: User,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "User ${user.displayName}" },
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCurrentUser) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.secondary
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.firstOrNull()?.toString()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                
                user.bio?.let { bio ->
                    Text(
                        text = bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Level badge
                Surface(
                    color = when (user.level) {
                        UserLevel.JUNIOR -> MaterialTheme.colorScheme.tertiary
                        UserLevel.MID -> MaterialTheme.colorScheme.secondary
                        UserLevel.SENIOR -> MaterialTheme.colorScheme.primary
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = when (user.level) {
                            UserLevel.JUNIOR -> stringResource(R.string.level_junior)
                            UserLevel.MID -> stringResource(R.string.level_mid)
                            UserLevel.SENIOR -> stringResource(R.string.level_senior)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Languages
                if (user.languages.isNotEmpty()) {
                    Text(
                        text = user.languages.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
