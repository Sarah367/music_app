package com.example.musicapp.navigation

object Routes {
    const val HOME = "home"
    const val PLAYLIST = "playlist/{playlistId}"
    const val TRACK = "playlist/{playlistId}/track/{trackId}"

    fun playlist(playlistId: String) = "playlist/$playlistId"

    fun track(playlistId: String, trackId: String) = "playlist/$playlistId/track/$trackId"
}