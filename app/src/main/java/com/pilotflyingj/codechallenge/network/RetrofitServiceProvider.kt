package com.pilotflyingj.codechallenge.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pilotflyingj.codechallenge.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitServiceProvider {
    private val contentType =  "application/json".toMediaType()

    private val json = Json { coerceInputValues = true }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): LocationService =
        retrofit.create(LocationService::class.java)
}