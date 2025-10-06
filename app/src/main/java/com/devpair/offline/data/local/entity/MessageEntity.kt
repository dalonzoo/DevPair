package com.devpair.offline.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devpair.offline.domain.model.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val senderId: String,
    val text: String,
    val timestamp: Long
)

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        sessionId = sessionId,
        senderId = senderId,
        text = text,
        timestamp = timestamp
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        sessionId = sessionId,
        senderId = senderId,
        text = text,
        timestamp = timestamp
    )
}
