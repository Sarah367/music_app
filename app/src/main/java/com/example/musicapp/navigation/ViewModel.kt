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
    val playlists: StateFlow<List<Playlist>> = _playlists
    val isLoading: StateFlow<Boolean> = _isLoading
    val playlistCount = _playlists.value.size
    fun sortingPlaylist(sortingMethod: String, playlistId: String){
        // val sortingOptions = listOf("A-Z", "Artist", "Album", "Year", "Length", "Popularity", "Default")
        val current = _playlists.value.toMutableList()
        val playlistIndex = current.indexOfFirst { it.playlistId == playlistId }
        val targetTracks = current[playlistIndex].tracks
        when(sortingMethod){
            "A-Z" -> {
                val sortedByTitle = targetTracks.sortedBy { it.name }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByTitle)
                _playlists.value = current
            }
            "Artist" -> {
                val sortedByArtist = targetTracks.sortedBy { it.artists[0].name }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByArtist)
                _playlists.value = current
            }
            "Album" -> {
                val sortedByAlbum = targetTracks.sortedBy { it.album.name }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByAlbum)
                _playlists.value = current
            }
            "Year" -> {
                val sortedByYear = targetTracks.sortedBy { it.album.releaseDate }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByYear)
                _playlists.value = current
            }
            "Length" -> {
                val sortedByLength = targetTracks.sortedBy { it.durationMs }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByLength)
                _playlists.value = current
            }
            "Popularity" -> {
                val sortedByPopularity = targetTracks.sortedBy { it.popularity }
                current[playlistIndex] = current[playlistIndex].copy(tracks = sortedByPopularity.reversed())
                _playlists.value = current
            }
            "Default" -> {
                _playlists.value = _defaultOrderPlaylists.value
            }
        }
    }

    init {
        loadMusic()
    }

    private fun loadMusic(){
        viewModelScope.launch {
            println("loadMusic")
            println(userMusicData.size)
            _playlists.value = userMusicData
            _defaultOrderPlaylists.value = userMusicData
        }
    }

//    fun setMusicData(jsonString: String){
//        val playlistListType = object : TypeToken<List<Playlist>>() {}.type
//        val userMusicData: List<Playlist> = Gson().fromJson(jsonString, playlistListType)
//        _defaultOrderPlaylists.value = userMusicData
//        _playlists.value = userMusicData
//        _isLoading.value = false
//    }
}