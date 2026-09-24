package com.example.autoconnect.data.media

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.R

data class DemoTrack(
    val title: String,
    val artist: String,
    val album: String,
    val uriString: String
)

object DemoMediaCatalog {
    fun getDemoTracks(context: Context): List<DemoTrack> {
        val rawUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.packageName)
            .appendPath("${R.raw.demo_track}")
            .build()
            .toString()

        return listOf(
            DemoTrack(
                title = "Neon Autoway",
                artist = "AutoConnect Synth Lab",
                album = "Infotainment Soundscapes",
                uriString = rawUri
            ),
            DemoTrack(
                title = "Electric Horizon",
                artist = "CyberPulse Ensemble",
                album = "Cruising Frequency",
                uriString = rawUri
            ),
            DemoTrack(
                title = "Midnight Battery Cruise",
                artist = "Kinetics & Drift",
                album = "EV Drive Audio",
                uriString = rawUri
            )
        )
    }
}
