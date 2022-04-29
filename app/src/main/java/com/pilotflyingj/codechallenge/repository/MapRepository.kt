package com.pilotflyingj.codechallenge.repository

import com.pilotflyingj.codechallenge.network.LocationService
import com.pilotflyingj.codechallenge.network.models.ApiSite
import javax.inject.Inject

class MapRepository @Inject constructor(private val service: LocationService) {

    suspend fun getAllLocations(): List<ApiSite> {
        return service.getLocations()
    }

}