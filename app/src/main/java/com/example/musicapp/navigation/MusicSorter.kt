package com.example.musicapp.navigation

import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import coil3.compose.AsyncImage

@Composable
fun MusicSorterApp(){
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "mainFlow"
    ){
        navigation(startDestination = Routes.HOME, "mainFlow"){
            composable(Routes.HOME) { backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }
                val musicDataViewModel: MusicDataViewModel = viewModel(mainFlowEntry)
                PlaylistScreen(musicDataViewModel, navController)
            }
            composable(Routes.PLAYLIST) { backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }
                val musicDataViewModel: MusicDataViewModel = viewModel(mainFlowEntry)
                val playlistId = backStackEntry.arguments?.getString("playlistId") ?: "" // elvis operator, otherwise null
                PlaylistTracksScreen(musicDataViewModel, navController, playlistId)
            }
            composable(Routes.TRACK) { backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }
                val musicDataViewModel: MusicDataViewModel = viewModel(mainFlowEntry)
                val playlistId = backStackEntry.arguments?.getString("playlistId") ?: "" // otherwise, null...
                val trackId = backStackEntry.arguments?.getString("trackId") ?: ""
                TrackInfoScreen(musicDataViewModel, navController, playlistId, trackId)
            }
        }
    }
}

@Composable
fun PlaylistScreen(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController
){
    val playlists by musicDataViewModel.playlists.collectAsState()
    val isLoading by musicDataViewModel.isLoading.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = "Music Explorer",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (isLoading) {
            // show that its loading the data...
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (playlists.isEmpty()) {
            // empty state is shown if loading fails.
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No playlists could be found.")
            }
        } else {
            //shows the playlists
            LazyColumn {
                items(playlists) { playlist ->
                    PlaylistCard(
                        musicDataViewModel = musicDataViewModel,
                        navController = navController,
                        playlist = playlist
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistCard(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    playlist: Playlist
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(Routes.playlist(playlist.playlistId))
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = playlist.playlistName,
                style=MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "${playlist.tracks.size} songs",
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Composable
fun PlaylistTracksScreen(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    playlistId: String
){
    val playlists by musicDataViewModel.playlists.collectAsState()
    val sortedState = musicDataViewModel.playlistSortedState(playlistId)
    val playlist = playlists.find {
        it.playlistId == playlistId // iterates through and finds the id of playlist.
    }

    if (playlist == null) { // if its null...
        Text("The playlist could not be found.")
        return
    }

    var expanded by remember { mutableStateOf(false)}
    var selectedSort by remember {mutableStateOf(sortedState)}
    val sortingOptions = listOf("A-Z", "Artist", "Album", "Year", "Length", "Popularity", "Default")

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

        }
        Text(
            text = playlist.playlistName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // sorting dropdown...
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { expanded = true }) {
                Text("Sort By: $selectedSort")
            } // just using simple drop down menu.
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sortingOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedSort = option
                            musicDataViewModel.sortingPlaylist(option, playlistId)
                            expanded = false
                        }
                    )
                }
            }
        }
        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(playlist.tracks) { track ->
                TrackCard(
                    musicDataViewModel = musicDataViewModel,
                    navController = navController,
                    track = track,
                    playlistId = playlistId
                )
            }
        }
    }
}

@Composable
fun TrackCard(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    track: Track,
    playlistId: String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate(Routes.track(playlistId, track.id))
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)


    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = track.artists.joinToString { it.name },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TrackInfoScreen(
    musicDataViewModel: MusicDataViewModel,
    navController: NavController,
    playlistId: String,
    trackId: String
) {
    val playlists by musicDataViewModel.playlists.collectAsState()
    val playlist = playlists.find { it.playlistId == playlistId }
    val track = playlist?.tracks?.find { it.id == trackId }

    if (track == null) {
        Text("The track could not be found.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.navigateUp()}
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
        Text(
            text = "Song Details",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = track.name.first().toString().uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
            AsyncImage(
                model = track.album.images[2].url,
                contentDescription = track.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // song title/info
        Text(
            text = track.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            TrackDetailRow("Artist", track.artists.joinToString{it.name})
            TrackDetailRow("Album", track.album.name)
            TrackDetailRow("Year", track.album.releaseDate.take(4)) // just take 4 characters of year
            TrackDetailRow("Listen", track.externalUrls.spotify)
        }
    }
}

@Composable
fun TrackDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        if(!value.contains("https://open.spotify.com/track")) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }else{
            val uriHandler = LocalUriHandler.current
            IconButton(
                onClick = { uriHandler.openUri(value) }
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Song")
            }
        }
    }
}

