package com.ys.basicandroid.data.di

import com.ys.basicandroid.data.repository.GithubRepositoryImpl
import com.ys.basicandroid.data.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Binds
    abstract fun bindGithubRepository(
        repository: GithubRepositoryImpl
    ): GithubRepository
}