package com.devpair.offline.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devpair.offline.domain.model.User
import com.devpair.offline.domain.model.UserLevel

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val avatarUrl: String?,
    val bio: String?,
    val languages: String, // CSV: "Kotlin,Java,Python"
    val level: String
)

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        displayName = displayName,
        avatarUrl = avatarUrl,
        bio = bio,
        languages = if (languages.isBlank()) emptyList() else languages.split(","),
        level = UserLevel.valueOf(level)
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        displayName = displayName,
        avatarUrl = avatarUrl,
        bio = bio,
        languages = languages.joinToString(","),
        level = level.name
    )
}
