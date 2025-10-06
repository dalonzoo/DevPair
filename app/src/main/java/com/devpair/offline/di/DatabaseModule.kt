package com.devpair.offline.di

import android.content.Context
import androidx.room.Room
import com.devpair.offline.data.local.DevPairDatabase
import com.devpair.offline.data.local.dao.MessageDao
import com.devpair.offline.data.local.dao.SessionDao
import com.devpair.offline.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DevPairDatabase {
        return Room.databaseBuilder(
            context,
            DevPairDatabase::class.java,
            "devpair_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(database: DevPairDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideSessionDao(database: DevPairDatabase): SessionDao {
        return database.sessionDao()
    }

    @Provides
    fun provideMessageDao(database: DevPairDatabase): MessageDao {
        return database.messageDao()
    }
}
