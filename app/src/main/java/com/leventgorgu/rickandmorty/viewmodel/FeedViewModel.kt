package com.leventgorgu.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leventgorgu.rickandmorty.api.RickAndMortyAPIService
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.location.Location
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FeedViewModel:ViewModel() {

    private var rickAndMortyAPIService = RickAndMortyAPIService()

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val _character = MutableLiveData<Character>()
    val character: LiveData<Character> = _character



    fun getLocations(){
        viewModelScope.launch(handler) {
            val data = rickAndMortyAPIService.getLocationsData()
            data.body().let {
               _location.value = it
            }
        }
    }

    fun getCharacters(characterIds: String){
        viewModelScope.launch(handler) {
            val data = rickAndMortyAPIService.getCharactersData(characterIds)
            if (data.isSuccessful){
                data.body()?.let { characterFromAPI ->
                    _character.value = characterFromAPI
                }
            }
        }
    }
    fun getCharacter(characterIds: String){
        viewModelScope.launch(handler) {
            val data = rickAndMortyAPIService.getCharacterData(characterIds)
            if (data.isSuccessful){
                data.body()?.let { characterItemFromAPI ->
                    val character = Character()
                    character.add(characterItemFromAPI)
                    _character.value = character
                }
            }
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
}