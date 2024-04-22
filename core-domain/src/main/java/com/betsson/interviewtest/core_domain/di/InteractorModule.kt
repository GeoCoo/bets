package com.betsson.interviewtest.core_domain.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class InteractorModule {

    @Provides
    fun provideMainInteractor(impl: com.betsson.interviewtest.core_domain.interactor.BetsInteractorImpl): com.betsson.interviewtest.core_domain.interactor.BetsInteractor = impl


}