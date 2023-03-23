package com.leventgorgu.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leventgorgu.rickandmorty.R
import com.leventgorgu.rickandmorty.api.RickAndMortyAPIService
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.location.Location
import com.leventgorgu.rickandmorty.model.location.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FeedViewModel:ViewModel() {

    private var rickAndMortyAPIService = RickAndMortyAPIService()

    private val _fragmentState = MutableLiveData<Boolean>()
    val fragmentState: LiveData<Boolean> = _fragmentState

    private val _pageNumber = MutableLiveData<String>()
    val pageNumber: LiveData<String> = _pageNumber

    private val _selectedRow = MutableLiveData<Int>()
    val selectedRow: LiveData<Int> = _selectedRow

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val _character = MutableLiveData<Character>()
    val character: LiveData<Character> = _character

    fun saveFragmentState(feedFragmentState:Boolean){
        _fragmentState.value = feedFragmentState
    }

    fun savePageNumber(pageNumberFromApi:String){
        _pageNumber.value = pageNumberFromApi
    }

    fun saveSelectedRow(selectedRowIndex:Int){
        _selectedRow.value = selectedRowIndex
    }


    fun getLocations(pageNumber:String){
        viewModelScope.launch(handler) {
            val data = rickAndMortyAPIService.getLocationsData(pageNumber)
            data.body()?.let { newLocationData ->
                if (_location.value == null){
                    _location.value = newLocationData
                }else{
                    val currentData = _location.value
                    currentData?.let {
                        currentData.results =  currentData.results + newLocationData.results
                        if (newLocationData.info.next != null){
                            currentData.info = newLocationData.info
                        }else{
                            currentData.info.next = "=0"
                        }
                        _location.value = currentData!!
                    }
                }
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