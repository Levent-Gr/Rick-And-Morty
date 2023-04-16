package com.leventgorgu.rickandmorty.repo

import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.model.location.Location
import com.leventgorgu.rickandmorty.utils.NetworkResult
import retrofit2.Response

interface RickAndMortyRepositoryInterface {

    suspend fun getLocationsData(pageNumber:String) : NetworkResult<Location>

    suspend fun getCharactersData(characterIds: String) : NetworkResult<Character>

    suspend fun getCharacterData(characterId: String) : NetworkResult<CharacterItem>
}