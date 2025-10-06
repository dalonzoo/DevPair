package com.devpair.offline.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devpair.offline.domain.model.Session
import com.devpair.offline.domain.model.SessionStatus

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val hostId: String,
    val guestId: String?,
    val language: String,
    val durationMinutes: Int,
    val status: String,
    val startedAt: Long?,
    val finishedAt: Long?
)

fun SessionEntity.toDomain(): Session {
    return Session(
        id = id,
        hostId = hostId,
        guestId = guestId,
        language = language,
        durationMinutes = durationMinutes,
        status = SessionStatus.valueOf(status),
        startedAt = startedAt,
        finishedAt = finishedAt
    )
}

fun Session.toEntity(): SessionEntity {
    return SessionEntity(
        id = id,
        hostId = hostId,
        guestId = guestId,
        language = language,
        durationMinutes = durationMinutes,
        status = status.name,
        startedAt = startedAt,
        finishedAt = finishedAt
    )
}
