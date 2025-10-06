package com.devpair.offline.data.repository

import com.devpair.offline.data.local.dao.SessionDao
import com.devpair.offline.data.local.entity.toDomain
import com.devpair.offline.data.local.entity.toEntity
import com.devpair.offline.data.util.RealtimeTicker
import com.devpair.offline.domain.model.Session
import com.devpair.offline.domain.model.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDao: SessionDao,
    private val userRepository: UserRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        // Osserva le sessioni WAITING e simula il matching automatico
        scope.launch {
            sessionDao.observeAllSessions()
                .map { list -> list.filter { it.status == SessionStatus.WAITING.name } }
                .flatMapConcat { waitingSessions ->
                    if (waitingSessions.isEmpty()) {
                        flowOf(emptyList())
                    } else {
                        RealtimeTicker.tickFlow().map { waitingSessions }
                    }
                }
                .collect { sessions ->
                    sessions.forEach { sessionEntity ->
                        // Dopo 1 tick, assegna guest fittizio e passa a ONGOING
                        val guestId = pickRandomGuest()
                        val updated = sessionEntity.copy(
                            guestId = guestId,
                            status = SessionStatus.ONGOING.name,
                            startedAt = System.currentTimeMillis()
                        )
                        sessionDao.updateSession(updated)
                    }
                }
        }
    }

    fun observeSession(sessionId: String): Flow<Session?> {
        return sessionDao.observeSession(sessionId).map { it?.toDomain() }
    }

    fun observeAllSessions(): Flow<List<Session>> {
        return sessionDao.observeAllSessions().map { list -> list.map { it.toDomain() } }
    }

    suspend fun getSession(sessionId: String): Session? {
        return sessionDao.getSession(sessionId)?.toDomain()
    }

    suspend fun createSession(
        hostId: String,
        language: String,
        durationMinutes: Int
    ): String {
        val sessionId = "session-${UUID.randomUUID()}"
        val session = Session(
            id = sessionId,
            hostId = hostId,
            guestId = null,
            language = language,
            durationMinutes = durationMinutes,
            status = SessionStatus.WAITING,
            startedAt = null,
            finishedAt = null
        )
        sessionDao.insertSession(session.toEntity())
        return sessionId
    }

    suspend fun updateSession(session: Session) {
        sessionDao.updateSession(session.toEntity())
    }

    suspend fun finishSession(sessionId: String) {
        val session = getSession(sessionId) ?: return
        val updated = session.copy(
            status = SessionStatus.FINISHED,
            finishedAt = System.currentTimeMillis()
        )
        updateSession(updated)
    }

    private suspend fun pickRandomGuest(): String {
        val allUsers = userRepository.getAllUsers()
        val candidates = allUsers.filter { 
            it.id != UserRepository.CURRENT_USER_ID && 
            it.id != UserRepository.BOT_USER_ID 
        }
        return candidates.randomOrNull()?.id ?: "user-2"
    }
}
