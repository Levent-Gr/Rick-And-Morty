package com.leventgorgu.rickandmorty.repo

import com.leventgorgu.rickandmorty.api.RickAndMortyAPI
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.model.location.Location
import com.leventgorgu.rickandmorty.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class RickAndMortyRepository @Inject constructor(private val rickAndMortyAPI: RickAndMortyAPI) :RickAndMortyRepositoryInterface{
    override suspend fun getLocationsData(pageNumber: String): NetworkResult<Location>{
        return try {
            val response = rickAndMortyAPI.getLocations(pageNumber)

            if (response.code() in 100..199) {
                return NetworkResult.loading(null)
            } else if (response.isSuccessful){
                response.body()?.let {
                    return@let NetworkResult.success(it)
                }?: return NetworkResult.error("Error",null)
            }else{
                return NetworkResult.error("Error",null)
            }
        }catch (e : Exception){
            return NetworkResult.error(e.localizedMessage?:"Error",null )
        }
    }

    override suspend fun getCharactersData(characterIds: String): NetworkResult<Character> {
        return try {
            val response = rickAndMortyAPI.getCharacters(characterIds)

            if (response.code() in 100..199) {
                return NetworkResult.loading(null)
            } else if (response.isSuccessful){
                response.body()?.let {
                    return@let NetworkResult.success(it)
                }?: return NetworkResult.error("Error",null)
            }else{
                return NetworkResult.error("Error",null)
            }
        }catch (e : Exception){
            return NetworkResult.error(e.localizedMessage?:"Error",null )
        }
    }

    override suspend fun getCharacterData(characterId: String): NetworkResult<CharacterItem> {
        return try {
            val response = rickAndMortyAPI.getCharacter(characterId)

            if (response.code() in 100..199) {
                return NetworkResult.loading(null)
            } else if (response.isSuccessful){
                response.body()?.let {
                    return@let NetworkResult.success(it)
                }?: return NetworkResult.error("Error",null)
            }else{
                return NetworkResult.error("Error",null)
            }
        }catch (e : Exception){
            return NetworkResult.error(e.localizedMessage?:"Error",null )
        }
    }
}