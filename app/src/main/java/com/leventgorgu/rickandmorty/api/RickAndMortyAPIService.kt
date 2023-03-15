package com.leventgorgu.rickandmorty.api

import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.model.location.Location
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RickAndMortyAPIService {

    private val BASE_URL = "https://rickandmortyapi.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RickAndMortyAPI::class.java)

    suspend fun getLocationsData() : Response<Location> {
        return api.getLocations()
    }
    suspend fun getCharactersData(characterIds: String) : Response<Character>{
        return api.getCharacters(characterIds)
    }

    suspend fun getCharacterData(characterId: String) : Response<CharacterItem>{
        return api.getCharacter(characterId)
    }
}