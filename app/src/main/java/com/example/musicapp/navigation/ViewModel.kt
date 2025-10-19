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
    val playlists: StateFlow<List<Playlist>> = _playlists
    val trackCount = {}
    val playlistCount = {}
    fun sortingPlaylist(sorting_method: String, playlist_id: String){
        return
    }

    fun setMusicData(jsonString: String){
        val playlistListType = object : TypeToken<List<Playlist>>() {}.type
        val userMusicData: List<Playlist> = Gson().fromJson(jsonString, playlistListType)
        _defaultOrderPlaylists.value = userMusicData
        _playlists.value = userMusicData
    }
}