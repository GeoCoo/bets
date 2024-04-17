package com.betsson.interviewtest.di


import com.betsson.interviewtest.screens.bets.BetsRepository
import com.betsson.interviewtest.screens.bets.BetsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(impl: BetsRepositoryImpl): BetsRepository = impl


}



