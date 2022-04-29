package com.pilotflyingj.codechallenge.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilotflyingj.codechallenge.network.models.ApiSite
import com.pilotflyingj.codechallenge.repository.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapsViewModel @ViewModelInject constructor(private val repository: MapRepository) :
    ViewModel() {
    private var mLocations = MutableLiveData<List<ApiSite>>()

    init {
        getLocationData()
    }

    private fun getLocationData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val locations = repository.getAllLocations()
                mLocations.postValue(locations)
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "getLocationData: ${e.message}")
            }

        }
    }

    fun locations() = mLocations

}