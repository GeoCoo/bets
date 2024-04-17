package com.betsson.interviewtest.helpers

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ResourceProvider {
    fun provideContext(): Context
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
//    fun genericErrorMessage(): String
//    fun genericNetworkErrorMessage(): String
    fun provideDrawableFromString(list: List<String>?): List<Int>?
}

class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {

    override fun provideContext() = context


    override fun provideDrawableFromString(list: List<String>?): List<Int>? {
        return list?.map {
            context.resources.getIdentifier(it, "drawable", context.packageName)
        }
    }

    override fun getString(@StringRes resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}