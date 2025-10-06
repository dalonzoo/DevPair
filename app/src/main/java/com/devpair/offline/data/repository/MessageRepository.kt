package com.devpair.offline.data.repository

import com.devpair.offline.data.local.dao.MessageDao
import com.devpair.offline.data.local.entity.toDomain
import com.devpair.offline.data.local.entity.toEntity
import com.devpair.offline.data.util.RealtimeTicker
import com.devpair.offline.domain.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageDao: MessageDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    // Tiene traccia dei messaggi utente per cui il bot deve rispondere
    private val pendingBotReplies = MutableStateFlow<List<Message>>(emptyList())

    init {
        // Bot responder: risponde dopo 1 tick ai messaggi non-bot
        scope.launch {
            pendingBotReplies
                .flatMapConcat { messages ->
                    if (messages.isEmpty()) {
                        flowOf(emptyList())
                    } else {
                        RealtimeTicker.tickFlow().map { messages }
                    }
                }
                .collect { messages ->
                    messages.forEach { userMessage ->
                        sendBotReply(userMessage)
                    }
                    pendingBotReplies.value = emptyList()
                }
        }
    }

    fun observeMessages(sessionId: String): Flow<List<Message>> {
        return messageDao.observeMessages(sessionId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun sendMessage(sessionId: String, senderId: String, text: String) {
        val message = Message(
            id = "msg-${UUID.randomUUID()}",
            sessionId = sessionId,
            senderId = senderId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        messageDao.insertMessage(message.toEntity())

        // Se il messaggio non è dal bot, schedula risposta bot
        if (senderId != UserRepository.BOT_USER_ID) {
            val current = pendingBotReplies.value.toMutableList()
            current.add(message)
            pendingBotReplies.value = current
        }
    }

    private suspend fun sendBotReply(originalMessage: Message) {
        val botReply = Message(
            id = "msg-${UUID.randomUUID()}",
            sessionId = originalMessage.sessionId,
            senderId = UserRepository.BOT_USER_ID,
            text = "Echo: ${originalMessage.text}",
            timestamp = System.currentTimeMillis()
        )
        messageDao.insertMessage(botReply.toEntity())
    }
}
