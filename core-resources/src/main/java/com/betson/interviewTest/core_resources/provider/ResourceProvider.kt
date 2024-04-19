package com.betson.interviewTest.core_resources.provider

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ResourceProvider {
    fun provideContext(): Context
    fun getString(@StringRes resId: Int): String
    fun provideDrawableFromString(name: String): Int?

}

class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {

    override fun provideContext() = context
    override fun getString(@StringRes resId: Int): String = context.getString(resId)
    override fun provideDrawableFromString(name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

}