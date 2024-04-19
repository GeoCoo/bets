package com.betsson.interviewtest.core_data.di


import com.betsson.interviewtest.core_data.repository.BetsRepository
import com.betsson.interviewtest.core_data.repository.BetsRepositoryImpl
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



