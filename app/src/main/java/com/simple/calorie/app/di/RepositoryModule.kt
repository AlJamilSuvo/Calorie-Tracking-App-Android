package com.simple.calorie.app.di

import com.simple.calorie.app.repositories.RemoteRepository
import com.simple.calorie.app.repositories.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: RemoteRepositoryImpl): RemoteRepository
}