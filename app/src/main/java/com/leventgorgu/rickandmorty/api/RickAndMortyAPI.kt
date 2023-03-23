package com.leventgorgu.rickandmorty.api

import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.model.location.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("/api/location")
    suspend fun getLocations(@Query("page")pageNumber:String):Response<Location>

    @GET("/api/character/{ids}")
    suspend fun getCharacters(@Path("ids") characterIds: String): Response<Character>

    @GET("/api/character/{id}")
    suspend fun getCharacter(@Path("id") characterId: String): Response<CharacterItem>

}