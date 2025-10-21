package com.example.musicapp.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

data class Playlist(
    @SerializedName("playlist_id")
    val playlistId: String,
    @SerializedName("playlist_name")
    val playlistName: String,
    val tracks: List<Track>
)

data class Track(
    val type: String,
    val track: Boolean,
    val album: Album,
    val artists: List<Artist>,
    @SerializedName("disc_number")
    val discNumber: Int,
    @SerializedName("track_number")
    val trackNumber: Int,
    @SerializedName("duration_ms")
    val durationMs: Int,
    @SerializedName("external_ids")
    val externalIds: ExternalIds,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    val uri: String,
    @SerializedName("is_local")
    val isLocal: Boolean
)

data class Album(
    val type: String,
    @SerializedName("album_type")
    val albumType: String,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("release_date_precision")
    val releaseDatePrecision: String,
    val uri: String,
    val artists: List<Artist>,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    @SerializedName("total_tracks")
    val totalTracks: Int
)

data class Artist(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class Image(
    val height: Int,
    val url: String,
    val width: Int
)

data class ExternalUrls(
    val spotify: String
)

data class ExternalIds(
    val isrc: String
)

val userMusicDataFun = { jsonString: String ->
    val playlistListType = object : TypeToken<List<Playlist>>() {}.type
    val readData: List<Playlist> = Gson().fromJson(jsonString, playlistListType)
    readData
}
var userMusicData: List<Playlist> = emptyList()
