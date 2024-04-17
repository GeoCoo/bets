package com.betsson.interviewtest.di


import com.betsson.interviewtest.helpers.ResourceProvider
import com.betsson.interviewtest.helpers.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ResourceModule {

    @Provides
    @Singleton
    fun provideResourceProvider(impl: ResourceProviderImpl): ResourceProvider = impl

    @Provides
    @Singleton
    fun provideCoroutineScopes(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }
}