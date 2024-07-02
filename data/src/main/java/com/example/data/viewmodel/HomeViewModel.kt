package com.example.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonData
import com.example.data.datamodels.PokemonDetails
import com.example.data.datamodels.PokemonResponse
import com.example.data.localstorage.FavoritePokemonEntity
import com.example.data.usecase.AddNewFavoritePokemonUseCase
import com.example.data.usecase.GetAllFavoritePokemonListUseCase
import com.example.data.usecase.GetNextPokemonListUseCase
import com.example.data.usecase.GetPokemonListUseCase
import com.example.data.usecase.GetPokemonSpritesUseCase
import com.example.data.usecase.GetPreviousPokemonListUseCase
import com.example.data.usecase.RemoveFavoritePokemonUseCase
import com.example.data.utils.Constants.Companion.BASE_MAIN_URL
import com.example.data.utils.Constants.Companion.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonSpritesUseCase: GetPokemonSpritesUseCase,
    private val getNextPokemonListUseCase: GetNextPokemonListUseCase,
    private val getPreviousPokemonListUseCase: GetPreviousPokemonListUseCase,
    private val getAllFavoritePokemonListUseCase: GetAllFavoritePokemonListUseCase,
    private val addNewFavoritePokemonUseCase: AddNewFavoritePokemonUseCase,
    private val removeFavoritePokemonUseCase: RemoveFavoritePokemonUseCase,
    private val loadingComponentViewModel : LoadingComponentViewModel
) : ViewModel() {


    private val _pokemonListState: MutableStateFlow<ArrayList<Pokemon>> = MutableStateFlow(
        arrayListOf()
    )
    val pokemonList: StateFlow<ArrayList<Pokemon>> = _pokemonListState.asStateFlow()

    private val _pokemonListDisplayState: MutableStateFlow<ArrayList<PokemonData>> =
        MutableStateFlow(
            arrayListOf()
        )
    val pokemonListDisplay: StateFlow<ArrayList<PokemonData>> =
        _pokemonListDisplayState.asStateFlow()

    private val _nextPokemonListUrlState: MutableStateFlow<String> = MutableStateFlow(EMPTY_STRING)
    private val nextPokemonListUrl: StateFlow<String> = _nextPokemonListUrlState.asStateFlow()

    private val _previousPokemonListUrlState: MutableStateFlow<String> =
        MutableStateFlow(EMPTY_STRING)
    private val previousPokemonListUrl: StateFlow<String> =
        _previousPokemonListUrlState.asStateFlow()

    private val _pokemonDataSelectedState: MutableStateFlow<PokemonData?> = MutableStateFlow(null)
    val pokemonDataSelected: StateFlow<PokemonData?> = _pokemonDataSelectedState.asStateFlow()


    private val _favoritePokemonListState: MutableStateFlow<List<FavoritePokemonEntity>> =
        MutableStateFlow(
            listOf()
        )
    private val favoritePokemonList: StateFlow<List<FavoritePokemonEntity>> = _favoritePokemonListState.asStateFlow()


    private val _showPreviousButtonState: MutableStateFlow<Boolean> =  MutableStateFlow( false )
    val showPreviousButton: StateFlow<Boolean> = _showPreviousButtonState.asStateFlow()

    private val _showNextButtonState: MutableStateFlow<Boolean> =  MutableStateFlow( false )
    val showNextButton: StateFlow<Boolean> = _showNextButtonState.asStateFlow()


    //private var repository : LocalStorageRepository
    init {
        getPokemonList()
        getAllFavoritePokemonList()
    }


    fun setPokemonDataSelected(data: PokemonData) {
        _pokemonDataSelectedState.value = data
    }

    private fun setPokemonList(pokemonList: ArrayList<Pokemon>) {
        _pokemonListState.value = pokemonList
    }

    private fun setPokemonDisplayList(pokemonList: ArrayList<PokemonData>) {
        _pokemonListDisplayState.value = pokemonList
    }

    private fun setNextPokemonListUrl(url: String) {
        _nextPokemonListUrlState.value = url
    }

    private fun setPreviousPokemonListUrl(url: String) {
        _previousPokemonListUrlState.value = url
    }

    private fun setFavoritePokemonList(favoritePokemonList: List<FavoritePokemonEntity>) {
        _favoritePokemonListState.value = favoritePokemonList
    }

    private fun getPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {

            val response = getPokemonListUseCase.invoke()
            processResponse(response = response)
        }.invokeOnCompletion {
            if (pokemonList.value.isNotEmpty()) {
                getSprites()
            }
        }
    }

    fun getNextPokemonItems() {
        if (hasValidValue(nextPokemonListUrl.value)) {
            val offset = getOffset(nextPokemonListUrl.value)
            viewModelScope.launch(Dispatchers.IO) {
                val response = getNextPokemonListUseCase.invoke(offset, 20)
                processResponse(response = response)
            }.invokeOnCompletion {
                if (pokemonList.value.isNotEmpty()) {
                    getSprites()
                }
            }
        }
    }

    fun getPreviousPokemonItems() {
        if (hasValidValue(previousPokemonListUrl.value)) {
            val offset = getOffset(previousPokemonListUrl.value)
            viewModelScope.launch(Dispatchers.IO) {
                val response = getPreviousPokemonListUseCase.invoke(offset, 20)
                processResponse(response = response)
            }.invokeOnCompletion {
                if (pokemonList.value.isNotEmpty()) {
                    getSprites()
                }
            }
        }
    }

    fun addNewFavoritePokemon(pokemon: PokemonData) {
        viewModelScope.launch(Dispatchers.IO) {
            addNewFavoritePokemonUseCase.invoke(pokemon)
        }.invokeOnCompletion {
            getAllFavoritePokemonList()
            _pokemonListDisplayState.value.find { pokemonDisplayed -> pokemonDisplayed.details.id == pokemon.details.id }
                ?.apply {
                    isFavorite = true
                }
        }
    }

    fun removeFavoritePokemon(pokemon: PokemonData) {
        viewModelScope.launch(Dispatchers.IO) {
            removeFavoritePokemonUseCase.invoke(pokemon)
        }.invokeOnCompletion {
            getAllFavoritePokemonList()
            _pokemonListDisplayState.value.find { pokemonDisplayed -> pokemonDisplayed.details.id == pokemon.details.id }
                ?.apply {
                    isFavorite = false
                }
        }
    }

    private fun getAllFavoritePokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoritePokemonList = getAllFavoritePokemonListUseCase.invoke()
            favoritePokemonList?.let {
                setFavoritePokemonList(favoritePokemonList)
            }
        }.invokeOnCompletion {
        }
    }


    private fun getSprites() {
        val tempPokemonDisplayList: ArrayList<PokemonData> = arrayListOf()
        viewModelScope.launch(Dispatchers.IO) {
            pokemonList.value.forEach {
                val pokemonDetails = getPokemonSpritesUseCase.invoke(
                    it.url.replace(
                        BASE_MAIN_URL + "pokemon/",
                        EMPTY_STRING
                    )
                )

                val isFavorite = isFavoritePokemon(pokemonDetails)
                pokemonDetails?.sprites?.let { sprite ->
                    tempPokemonDisplayList.add(
                        PokemonData(
                            name = it.name,
                            urlImage = sprite.frontDefault,
                            details = pokemonDetails,
                            isFavorite = isFavorite
                        )
                    )
                }
            }
        }.invokeOnCompletion {
            setPokemonDisplayList(tempPokemonDisplayList)
        }

    }

    private fun getOffset(url: String): Int {
        var offset: Int = 20
        if (hasValidValue(url)) {
            val parameters: String = url.split("?")[1]
            val numbers = parameters.split("&")
            offset = numbers[0].replace("offset=", EMPTY_STRING).toInt()
        }
        return offset
    }

    private fun hasValidValue(data: String?): Boolean {
        return data != EMPTY_STRING && data != "null" && data != null
    }

    private fun processResponse(response: PokemonResponse?) {
        if (response?.results?.isNotEmpty() == true) {
            if (hasValidValue(response.next)) {
                setNextPokemonListUrl(response.next)
                _showNextButtonState.value = true
            } else {
                setNextPokemonListUrl(EMPTY_STRING)
                _showNextButtonState.value = false
            }

            if (hasValidValue(response.previous)) {
                setPreviousPokemonListUrl(response.previous)
                _showPreviousButtonState.value = true
            } else {
                setPreviousPokemonListUrl(EMPTY_STRING)
                _showPreviousButtonState.value = false
            }
            setPokemonList(response.results)
        }
    }

    private fun isFavoritePokemon(pokemonDetails: PokemonDetails?): Boolean {
        var isFavorite: Boolean = false
        if (favoritePokemonList.value.isNotEmpty()) {
            isFavorite =
                favoritePokemonList.value.find { favoritePokemon -> favoritePokemon.id == pokemonDetails?.id } != null
        }
        return isFavorite
    }
}