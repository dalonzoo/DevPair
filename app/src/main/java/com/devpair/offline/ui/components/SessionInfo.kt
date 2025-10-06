package com.devpair.offline.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.devpair.offline.R
import com.devpair.offline.domain.model.Session
import com.devpair.offline.domain.model.SessionStatus

@Composable
fun SessionInfo(
    session: Session,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .semantics { 
                contentDescription = "Session in ${session.language} for ${session.durationMinutes} minutes" 
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Status badge
            val statusText = when (session.status) {
                SessionStatus.WAITING -> stringResource(R.string.waiting_partner)
                SessionStatus.ONGOING -> stringResource(R.string.session_ongoing)
                SessionStatus.FINISHED -> stringResource(R.string.session_finished)
            }
            val statusColor = when (session.status) {
                SessionStatus.WAITING -> MaterialTheme.colorScheme.tertiary
                SessionStatus.ONGOING -> MaterialTheme.colorScheme.primary
                SessionStatus.FINISHED -> MaterialTheme.colorScheme.outline
            }
            
            Surface(
                color = statusColor,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Language
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "Language icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = session.language,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Duration
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Duration icon",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${session.durationMinutes} ${stringResource(R.string.minutes_short)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
