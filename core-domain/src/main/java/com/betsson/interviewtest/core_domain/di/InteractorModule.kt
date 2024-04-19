package com.betsson.interviewtest.core_domain.di


import com.betsson.interviewtest.core_domain.interactor.MainInteractor
import com.betsson.interviewtest.core_domain.interactor.MainInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class InteractorModule {

    @Provides
    fun provideMainInteractor(impl: com.betsson.interviewtest.core_domain.interactor.MainInteractorImpl): com.betsson.interviewtest.core_domain.interactor.MainInteractor = impl


}