package com.devpair.offline.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devpair.offline.data.local.dao.MessageDao
import com.devpair.offline.data.local.dao.SessionDao
import com.devpair.offline.data.local.dao.UserDao
import com.devpair.offline.data.local.entity.MessageEntity
import com.devpair.offline.data.local.entity.SessionEntity
import com.devpair.offline.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SessionEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DevPairDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun messageDao(): MessageDao
}
