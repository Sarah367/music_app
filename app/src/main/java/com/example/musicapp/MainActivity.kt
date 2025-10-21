package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicapp.ui.theme.MusicAppTheme
import com.example.musicapp.navigation.MusicSorterApp
import com.example.musicapp.navigation.userMusicData
import com.example.musicapp.navigation.userMusicDataFun

//import com.example.musicapp.viewmodel.ViewPlaylist
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyMusic(modifier = Modifier.padding(innerPadding))
                }
            }
        }

    }
    override fun onStart() {
        super.onStart()
        println("OnStart has been called.")
    }
}

// View Model is commented out for now ...
//@Composable
//fun ViewScreen(modifier: Modifier) {
//    Column(modifier = modifier) {
//        ViewPlaylist()
//    }
//}

@Composable
fun MyMusic(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val json = context.assets.open("user_playlists.json").bufferedReader().use { it.readText() } //set music data (look at line 15)
    userMusicData = userMusicDataFun(json)
    println("Hello?")
    Column(modifier = modifier) {
        MusicSorterApp()
    }
}

@Preview(showBackground = true)
@Composable
fun MyMusicPreview() {
    MusicAppTheme {
        MyMusic()
    }
}