package com.devpair.offline.domain.model

data class Session(
    val id: String,
    val hostId: String,
    val guestId: String? = null,
    val language: String,
    val durationMinutes: Int,
    val status: SessionStatus = SessionStatus.WAITING,
    val startedAt: Long? = null,
    val finishedAt: Long? = null
)
