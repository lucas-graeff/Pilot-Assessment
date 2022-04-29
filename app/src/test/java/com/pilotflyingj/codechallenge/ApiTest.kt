package com.pilotflyingj.codechallenge

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pilotflyingj.codechallenge.network.LocationService
import com.pilotflyingj.codechallenge.repository.MapRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
class ApiTest {
    private lateinit var mLocationRepo: MapRepository


    private val mMockWebServer = MockWebServer()


    private val contentType =  "application/json".toMediaType()

    private val json = Json { coerceInputValues = true }

    private val mApi = Retrofit.Builder()
        .baseUrl(mMockWebServer.url("/"))
        .addConverterFactory(json.asConverterFactory(contentType)).build().create(LocationService::class.java)



    @Before
    fun setup() {
        mLocationRepo = MapRepository(mApi)
    }

    @After
    fun cleanUp() {
        mMockWebServer.shutdown()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testSuccessfulRepositoryResponse()  {
        mMockWebServer.apply {
            enqueue(MockResponse().setBody(MockFileReader("locations.json").getFakeJsonResponse()))
        }

        val response = runBlocking{ mLocationRepo.getAllLocations()}

        Assert.assertNotNull(response)
        Assert.assertEquals(response.size, 345 )

    }

}