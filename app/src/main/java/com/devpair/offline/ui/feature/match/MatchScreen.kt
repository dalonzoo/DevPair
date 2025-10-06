package com.devpair.offline.ui.feature.match

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devpair.offline.R
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    onNavigateBack: () -> Unit,
    onSessionCreated: (String) -> Unit,
    viewModel: MatchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.match_title)) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Language input
            OutlinedTextField(
                value = uiState.language,
                onValueChange = { viewModel.updateLanguage(it) },
                label = { Text(stringResource(R.string.language)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Language input field" },
                singleLine = true,
                enabled = !uiState.isCreating
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Duration slider
            Text(
                text = stringResource(R.string.duration),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiState.durationMinutes} ${stringResource(R.string.minutes_short)}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Slider(
                value = uiState.durationMinutes.toFloat(),
                onValueChange = { viewModel.updateDuration(it.roundToInt()) },
                valueRange = 15f..60f,
                steps = 8,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Duration slider" },
                enabled = !uiState.isCreating
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { viewModel.createSession(onSessionCreated) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .semantics { contentDescription = "Create session button" },
                enabled = !uiState.isCreating && uiState.language.isNotBlank()
            ) {
                if (uiState.isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.create_session),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We'll match you with a partner automatically",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
