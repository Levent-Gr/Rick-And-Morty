package com.leventgorgu.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.repo.RickAndMortyRepositoryInterface
import com.leventgorgu.rickandmorty.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val rickAndMortyRepository: RickAndMortyRepositoryInterface):ViewModel() {

    private val _character = MutableLiveData<NetworkResult<CharacterItem>>()
    val character: LiveData<NetworkResult<CharacterItem>> = _character

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