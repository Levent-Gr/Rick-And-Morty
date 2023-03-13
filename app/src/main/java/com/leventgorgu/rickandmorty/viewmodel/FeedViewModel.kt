package com.leventgorgu.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leventgorgu.rickandmorty.api.RickAndMortyAPIService
import com.leventgorgu.rickandmorty.model.location.Location
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FeedViewModel:ViewModel() {

    var rickAndMortyAPIService = RickAndMortyAPIService()

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    fun getLocations(){
        viewModelScope.launch(handler) {
            val data = rickAndMortyAPIService.getLocationsData()
            data.body().let {
               _location.value = it
            }
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
}