package com.leventgorgu.rickandmorty.repo

import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.model.character.Origin
import com.leventgorgu.rickandmorty.model.location.Location
import com.leventgorgu.rickandmorty.model.location.İnfo
import com.leventgorgu.rickandmorty.utils.NetworkResult


class FakeRickAndMortyRepository:RickAndMortyRepositoryInterface {

    override suspend fun getLocationsData(pageNumber: String): NetworkResult<Location> {
        return NetworkResult.success(Location(İnfo(0,"",0,1), listOf()))
    }

    override suspend fun getCharactersData(characterIds: String): NetworkResult<Character> {
        return NetworkResult.success(Character())
    }

    override suspend fun getCharacterData(characterId: String): NetworkResult<CharacterItem> {
        return NetworkResult.success(CharacterItem("", listOf(),"",0,"",com.leventgorgu.rickandmorty.model.character.Location("",""),"",
            Origin("",""),"","","",""))
    }
}