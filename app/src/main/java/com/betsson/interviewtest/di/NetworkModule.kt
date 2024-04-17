package com.betsson.interviewtest.di

import android.content.Context
import com.betsson.interviewtest.helpers.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /**
     * Constructs and provides a singleton [Retrofit] instance used to perform api calls.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl("https://example.com")//This is a dummy url in order retrofit to init. All application urls are set dynamically in Interceptor
        .client(okHttpClient)
        .build()


    /**
     * Constructs and provides a singleton [OkHttpClient] used for [Retrofit] instance
     * initialization.
     *
     * @return A [OkHttpClient] instance.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        resourceProvider: ResourceProvider
    ): OkHttpClient {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val response = Response.Builder()
                    .code(200)
                    .message("Mock response")
                    .request(request)
                    .protocol(Protocol.HTTP_1_0)
                    .body(
                        ResponseBody.create(
                            "application/json".toMediaTypeOrNull(),
                            resourceProvider.provideContext().assets.open("mockJsonResponse.json").bufferedReader()
                                .use { it.readText() }
                        )
                    )
                    .build()
                response
            }

        return client.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    /** Constructs and provides a singleton [HttpLoggingInterceptor] instance.
     *
     * @return A [HttpLoggingInterceptor] instance.
     */
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(
    ) = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY

        }


}

