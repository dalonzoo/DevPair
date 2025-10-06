package com.devpair.offline.domain.model

data class Message(
    val id: String,
    val sessionId: String,
    val senderId: String,
    val text: String,
    val timestamp: Long
)
