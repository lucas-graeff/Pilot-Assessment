package com.pilotflyingj.codechallenge.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pilotflyingj.codechallenge.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitServiceProvider {
    // TODO: create the API service lazily and retain in memory

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val contentType =  "application/json".toMediaType()

    private val json = Json { coerceInputValues = true }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): LocationService =
        retrofit.create(LocationService::class.java)
}