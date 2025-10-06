package com.devpair.offline.data.repository

import com.devpair.offline.data.local.dao.UserDao
import com.devpair.offline.data.local.entity.UserEntity
import com.devpair.offline.data.local.entity.toDomain
import com.devpair.offline.data.local.entity.toEntity
import com.devpair.offline.domain.model.User
import com.devpair.offline.domain.model.UserLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun observeUser(userId: String): Flow<User?> {
        return userDao.observeUser(userId).map { it?.toDomain() }
    }

    fun observeAllUsers(): Flow<List<User>> {
        return userDao.observeAllUsers().map { list -> list.map { it.toDomain() } }
    }

    suspend fun getUser(userId: String): User? {
        return userDao.getUser(userId)?.toDomain()
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers().map { it.toDomain() }
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    suspend fun insertUsers(users: List<User>) {
        userDao.insertUsers(users.map { it.toEntity() })
    }

    suspend fun seedUsersIfEmpty() {
        if (userDao.getAllUsers().isEmpty()) {
            val seedUsers = listOf(
                User(
                    id = "user-1",
                    displayName = "Marco Rossi",
                    bio = "Full-stack developer passionate about Kotlin",
                    languages = listOf("Kotlin", "Java", "TypeScript"),
                    level = UserLevel.SENIOR
                ),
                User(
                    id = "user-2",
                    displayName = "Giulia Bianchi",
                    bio = "Android enthusiast and Jetpack Compose lover",
                    languages = listOf("Kotlin", "Swift", "Dart"),
                    level = UserLevel.MID
                ),
                User(
                    id = "user-3",
                    displayName = "Luca Verdi",
                    bio = "Learning backend development",
                    languages = listOf("Python", "Go", "Kotlin"),
                    level = UserLevel.JUNIOR
                ),
                User(
                    id = "bot-assistant",
                    displayName = "DevBot",
                    bio = "Your friendly pair programming assistant",
                    languages = listOf("All"),
                    level = UserLevel.SENIOR
                )
            )
            insertUsers(seedUsers)
        }
    }

    companion object {
        const val CURRENT_USER_ID = "user-1" // Utente corrente mock
        const val BOT_USER_ID = "bot-assistant"
    }
}
