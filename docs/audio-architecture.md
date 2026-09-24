# Android Automotive Audio & Media3 Architecture

## Infotainment Audio Subsystem

In Android Automotive OS, audio routing is governed by CarAudioService, which manages multiple audio zones (Driver, Passenger, Rear Seat Entertainment) and audio focus rules (e.g. Navigation ducking media, Emergency warnings interrupting all streams).

```text
       +-----------------------------------------------------+
       |                   MediaScreen UI                    |
       |      (Compose UI, Transport Buttons, Progress)      |
       +--------------------------+--------------------------+
                                  |
                                  v
       +-----------------------------------------------------+
       |                   MediaViewModel                    |
       |            (Exposes StateFlow<MediaState>)          |
       +--------------------------+--------------------------+
                                  |
                                  v
       +-----------------------------------------------------+
       |              Media Use Cases / Repository           |
       |             (Play, Pause, Skip, Seek, Volume)       |
       +--------------------------+--------------------------+
                                  |
                                  v
       +-----------------------------------------------------+
       |                 AndroidX Media3                     |
       |               - ExoPlayer Engine                    |
       |               - MediaSession & Controllers          |
       +--------------------------+--------------------------+
                                  |
                                  v
       +-----------------------------------------------------+
       |               Local Audio Waveform                  |
       |             (res/raw/demo_track.wav)                |
       +-----------------------------------------------------+
```

## Media3 Implementation Details

- **ExoPlayer Engine**: Initialized on the main thread and attached to local raw audio resources (`res/raw/demo_track.wav`).
- **Real Playback Observation**: Listens to `Player.Listener.onIsPlayingChanged` and `onPlaybackStateChanged`.
- **Zero Mock Values**: When playback starts, real track time and duration advance smoothly via state flow, preventing desync between dashboard and media player screens.
