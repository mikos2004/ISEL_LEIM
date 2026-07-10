package dam.a50746.disneyjournal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.a50746.disneyjournal.api.fetchCharacters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import dam.a50746.disneyjournal.api.CharacterApiResponse
import dam.a50746.disneyjournal.api.CharacterData

class CharacterSearchViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<CharacterData>>(emptyList())
    val searchResults: StateFlow<List<CharacterData>> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Aguarda 300ms após a última digitação
                .distinctUntilChanged()
                .filter { query -> query.isNotEmpty() }
                .flatMapLatest { query ->
                    flow {
                        _isSearching.value = true
                        val results = searchCharacters(query)
                        emit(results)
                        _isSearching.value = false
                    }
                }
                .collect { results ->
                    _searchResults.value = results
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
        }
    }

    private suspend fun searchCharacters(query: String): List<CharacterData> {
        return try {
            val response: CharacterApiResponse = fetchCharacters(query)
            println("API Response data: ${response.data}")
            response.data.forEach { character ->
                println("Character: ${character.name}, ImageURL: ${character.imageUrl}")
            }
            response.data
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}