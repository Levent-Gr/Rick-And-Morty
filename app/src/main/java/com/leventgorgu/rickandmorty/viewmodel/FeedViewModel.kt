package com.leventgorgu.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.model.location.Location
import com.leventgorgu.rickandmorty.repo.RickAndMortyRepositoryInterface
import com.leventgorgu.rickandmorty.utils.NetworkResult
import com.leventgorgu.rickandmorty.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val rickAndMortyRepository: RickAndMortyRepositoryInterface):ViewModel() {

    private val _fragmentState = MutableLiveData<Boolean>()
    val fragmentState: LiveData<Boolean> = _fragmentState

    private val _pageNumber = MutableLiveData<String>()
    val pageNumber: LiveData<String> = _pageNumber

    private val _selectedRow = MutableLiveData<Int>()
    val selectedRow: LiveData<Int> = _selectedRow

    private val _location = MutableLiveData<NetworkResult<Location>>()
    val location: LiveData<NetworkResult<Location>> = _location

    private val _characters = MutableLiveData<NetworkResult<Character>>()
    val characters: LiveData<NetworkResult<Character>> = _characters

    private val _character = MutableLiveData<NetworkResult<CharacterItem>>()
    val character: LiveData<NetworkResult<CharacterItem>> = _character

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
            val locationsData = rickAndMortyRepository.getLocationsData(pageNumber)
            if (_location.value == null){
                if (locationsData.data !=null)
                    _location.value = NetworkResult.success(locationsData.data)
                else if(locationsData.status == Status.ERROR)
                    _location.value = NetworkResult.error("Error",null)
                else if (locationsData.status == Status.LOADING){
                    _location.value = NetworkResult.loading(null)
                }
            }else{
                val currentData = _location.value
                currentData?.let {
                    when(locationsData.status){
                        Status.SUCCESS ->{
                            currentData.data!!.results = currentData.data.results + locationsData.data!!.results
                            if (locationsData.data.info.next != null){
                                currentData.data.info = locationsData.data.info
                            }else{
                                currentData.data.info.next = "=0"
                            }
                            _location.value = currentData!!
                        }
                        Status.ERROR ->{
                            currentData.status = locationsData.status
                            _location.value = currentData!!
                        }
                        Status.LOADING->{
                            currentData.status = locationsData.status
                            _location.value = currentData!!
                        }
                    }

                }
            }
        }
    }

    fun getCharacters(characterIds: String){
        viewModelScope.launch(handler) {
            val charactersData = rickAndMortyRepository.getCharactersData(characterIds)
            charactersData.let {
                _characters.value = it
            }
        }
    }
    fun getCharacter(characterIds: String){
        viewModelScope.launch(handler) {
            val characterData= rickAndMortyRepository.getCharacterData(characterIds)
            characterData.let {
                _character.value = it
            }
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
}