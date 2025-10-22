package com.example.musicapp.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MusicDataViewModel: ViewModel() {
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    private val _defaultOrderPlaylists = MutableStateFlow<List<Playlist>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _sortedStates = MutableStateFlow<List<String>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists
    val playlistSortedState = { playlistId: String ->
        val current = _playlists.value.toMutableList()
        val playlistIndex = current.indexOfFirst { it.playlistId == playlistId }
        _sortedStates.value[playlistIndex]
    }
    val isLoading: StateFlow<Boolean> = _isLoading
    val playlistCount = _playlists.value.size
    fun sortingPlaylist(sortingMethod: String, playlistId: String){
        val current = _playlists.value.toMutableList()
        val playlistIndex = current.indexOfFirst { it.playlistId == playlistId }
        val targetTracks = current[playlistIndex].tracks
        when(sortingMethod){
            "A-Z" -> {
                val sortedByTitle = targetTracks.sortedBy { it.name }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByTitle)
                _playlists.value = current
                val curSortedStates = _sortedStates.value.toMutableList()
                curSortedStates[playlistIndex] = "A-Z"
                _sortedStates.value = curSortedStates
            }
            "Artist" -> {
                val sortedByArtist = targetTracks.sortedBy { it.artists[0].name }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByArtist)
                _playlists.value = current
                val curSortedStates = _sortedStates.value.toMutableList()
                curSortedStates[playlistIndex] = "Artist"
                _sortedStates.value = curSortedStates
            }
            "Album" -> {
                val sortedByAlbum = targetTracks.sortedBy { it.album.name }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByAlbum)
                _playlists.value = current
                val curSortedStates = _sortedStates.value.toMutableList()
                curSortedStates[playlistIndex] = "Album"
                _sortedStates.value = curSortedStates
            }
            "Year" -> {
                val sortedByYear = targetTracks.sortedBy { it.album.releaseDate }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByYear)
                _playlists.value = current
                val curSortedStates = _sortedStates.value.toMutableList()
                curSortedStates[playlistIndex] = "Year"
                _sortedStates.value = curSortedStates
            }
            "Length" -> {
                val sortedByLength = targetTracks.sortedBy { it.durationMs }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByLength)
                _playlists.value = current
                val curSortedStates = _sortedStates.value.toMutableList()
                curSortedStates[playlistIndex] = "Length"
                _sortedStates.value = curSortedStates
            }
            "Popularity" -> {
                val sortedByPopularity = targetTracks.sortedBy { it.popularity }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByPopularity.reversed())
                _playlists.value = current
                val curSortedStates = _sortedStates.value.toMutableList()
                curSortedStates[playlistIndex] = "Popularity"
                _sortedStates.value = curSortedStates
            }
            "Default" -> {
                _playlists.value = _defaultOrderPlaylists.value
                val curSortedStates = _sortedStates.value.toMutableList()
                curSortedStates[playlistIndex] = "Default"
                _sortedStates.value = curSortedStates
            }
        }
    }

    init {
        loadMusic()
    }

    private fun loadMusic(){
        viewModelScope.launch {
            _playlists.value = userMusicData
            _defaultOrderPlaylists.value = userMusicData
            _sortedStates.value = List(userMusicData.size) {"Default"}
        }
    }
}