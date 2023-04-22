package com.leventgorgu.rickandmorty.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.leventgorgu.rickandmorty.MainCoroutineRule
import com.leventgorgu.rickandmorty.getOrAwaitValueTest
import com.leventgorgu.rickandmorty.repo.FakeRickAndMortyRepository
import com.leventgorgu.rickandmorty.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeedViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FeedViewModel

    @Before
    fun setup(){
        viewModel = FeedViewModel(FakeRickAndMortyRepository())
    }

    @Test
    fun getLocationsDataTest(){
        val pageNumber = "1"
        viewModel.getLocations(pageNumber)
        val data = viewModel.location.getOrAwaitValueTest()

        assertThat(data.status).isEqualTo(Status.SUCCESS)

    }

    @Test
    fun getLocationsDataTestDataIsNotNull() {
        val pageNumber = "1"
        viewModel.getLocations(pageNumber)
        val data = viewModel.location.getOrAwaitValueTest()

        assertThat(data.data).isNotNull()
    }

    @Test
    fun getCharactersDataTest(){
        val charactersId = "1,2,3"
        viewModel.getCharacters(charactersId)
        val data = viewModel.characters.getOrAwaitValueTest()

        assertThat(data.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun getCharactersDataTestIsNotNull(){
        val charactersId = "1,2,3"
        viewModel.getCharacters(charactersId)
        val data = viewModel.characters.getOrAwaitValueTest()

        assertThat(data.data).isNotNull()
    }


    @Test
    fun getCharacterDataTest(){
        val characterId = "1"
        viewModel.getCharacter(characterId)
        val data = viewModel.character.getOrAwaitValueTest()

        assertThat(data.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun getCharacterDataTestIsNotNull(){
        val characterId = "1"
        viewModel.getCharacter(characterId)
        val data = viewModel.character.getOrAwaitValueTest()

        assertThat(data.data).isNotNull()
    }

}
