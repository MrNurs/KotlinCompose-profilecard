package com.example.profile.di

import android.content.Context
import androidx.room.Room
import com.example.profile.data.UserRepository
import com.example.profile.data.local.AppDatabase
import com.example.profile.data.local.UserDao
import com.example.profile.data.remote.UsersApi
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "profile.db"
        ).build()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideUserRepository(
        dao: UserDao,
        api: UsersApi
    ): UserRepository = UserRepository(dao, api)
}
