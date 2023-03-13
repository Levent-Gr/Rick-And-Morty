package com.leventgorgu.rickandmorty.api

import com.leventgorgu.rickandmorty.model.location.Location
import retrofit2.Response
import retrofit2.http.GET

interface RickAndMortyAPI {

    @GET("/api/location")
    suspend fun getLocations():Response<Location>
}