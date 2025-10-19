package com.example.musicapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

@Composable
fun MusicSorterApp(){
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val json = context.assets.open("user_playlists.json").bufferedReader().use { it.readText() }
//        viewModel.setMusicData(json)
    }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "mainFlow"
    ){
        navigation(startDestination = Routes.HOME, "mainFlow"){
            composable(Routes.HOME) {  }
            composable(Routes.PLAYLIST) {  }
            composable(Routes.TRACK) {  }
        }
    }
}

@Composable
fun PlaylistScreen(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController
){

}

@Composable
fun PlaylistCard(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    playlist: Playlist
){

}

@Composable
fun PlaylistTracksScreen(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    playlistId: String
){

}

@Composable
fun TrackCard(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    track: Track
){

}

@Composable
fun TrackInfoScreen(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    playlistId: String,
    trackId: String
){

}