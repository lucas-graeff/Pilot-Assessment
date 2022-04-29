package com.pilotflyingj.codechallenge

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pilotflyingj.codechallenge.network.LocationService
import com.pilotflyingj.codechallenge.network.models.ApiSite
import com.pilotflyingj.codechallenge.repository.MapRepository
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class ApiTest {

    private lateinit var response: List<ApiSite>
    private lateinit var mapRepository: MapRepository

    private val mockWebServer = MockWebServer()

    private val contentType =  "application/json".toMediaType()

    private val json = Json { coerceInputValues = true }

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
        .create(LocationService::class.java)

    @ExperimentalCoroutinesApi
    @Test
    fun `correct api response returns with size of 345`()  {
        mapRepository = MapRepository(api)

        mockWebServer.apply {
            enqueue(MockResponse().setBody(MockFileReader("locations.json").getFakeJsonResponse()))
        }

        GlobalScope.launch {
            response = mapRepository.getAllLocations()
            Assert.assertNotNull(response)
            Assert.assertEquals(response.size, 345 )
        }
    }

}