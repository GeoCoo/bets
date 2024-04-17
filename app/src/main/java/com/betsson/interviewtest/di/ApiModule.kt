package com.betsson.interviewtest.di

import com.betsson.interviewtest.networking.ApiClient
import com.betsson.interviewtest.networking.ApiClientImpl
import com.betsson.interviewtest.networking.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiClient(impl: ApiClientImpl): ApiClient = impl
}