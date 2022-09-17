package com.ys.basicandroid.data.di

import com.ys.basicandroid.data.repository.GithubRepositoryImpl
import com.ys.basicandroid.domain.repository.GithubRepository
import com.ys.basicandroid.domain.repository.SearchRepository
import com.ys.basicandroid.data.repository.SearchRepositoryImpl
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

    @Binds
    abstract fun bindSearchRepository(
        repository: SearchRepositoryImpl
    ): SearchRepository
}