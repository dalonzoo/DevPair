package com.devpair.offline.domain.model

data class User(
    val id: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val languages: List<String> = emptyList(),
    val level: UserLevel = UserLevel.MID
)
