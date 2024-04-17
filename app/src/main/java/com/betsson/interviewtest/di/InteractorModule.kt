package com.betsson.interviewtest.di


import com.betsson.interviewtest.screens.main.MainInteractor
import com.betsson.interviewtest.screens.main.MainInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class InteractorModule {

    @Provides
    fun provideMainInteractor(impl: MainInteractorImpl): MainInteractor = impl


}