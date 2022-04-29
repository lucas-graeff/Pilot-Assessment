package com.pilotflyingj.codechallenge.network

import com.pilotflyingj.codechallenge.network.models.ApiSite
import com.pilotflyingj.codechallenge.utils.Constants
import retrofit2.http.GET

interface LocationService {
    @GET(Constants.LOCATIONS)
    suspend fun getLocations(): List<ApiSite>
}