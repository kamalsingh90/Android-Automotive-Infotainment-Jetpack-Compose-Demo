# AutoConnect

### Android Automotive & Infotainment Demo

AutoConnect is a complete Android Automotive-style infotainment application designed to demonstrate senior-level software engineering across modern Android architecture, Clean Architecture, Jetpack Compose, Material 3, Android Framework, AndroidX Media3, local persistence with Preferences DataStore, and vehicle telemetry simulation.

> **Automotive Simulation Disclaimer:**  
> Vehicle signals are simulated for demonstration purposes. This project does not implement production vehicle control, ECU communication, CAN communication, Vehicle HAL, or safety-critical automotive functionality.

---

## Architecture Overview

AutoConnect is strictly organized according to **Clean Architecture** and **MVVM** principles:

```text
                    PRESENTATION LAYER
         (Jetpack Compose Screens & M3 Components)
                         |
                         v
                    VIEWMODEL
         (StateFlow, Lifecycle-aware observation)
                         |
                         v
                    USE CASES
         (Domain Validation, Telemetry, Media, Settings)
                         |
                         v
                REPOSITORY INTERFACE
                         |
                         v
              REPOSITORY IMPLEMENTATION
                         |
                         v
                    DATA SOURCES
           -------------------------------
           |                             |
           v                             v
      Android Framework /           Simulator /
     DataStore / Media3            Signal Manager
```

### Core Layers

1. **Presentation Layer (`feature/*`, `core/ui/*`)**:
   - Built exclusively in **Jetpack Compose** with **Material Design 3**.
   - Automotive dark theme optimized for high contrast and readability on in-dash displays.
   - Reusable `VehicleSpeedGauge` Canvas component with smooth pointer animation and ticks.
   - Dynamic screens: Dashboard, Vehicle Simulator, Media Player, Navigation Simulator, Settings, and System Diagnostics.

2. **Domain Layer (`domain/*`)**:
   - Enterprise business rules: validates vehicle readiness, prevents engine start with parking brake engaged, enforces minimum battery levels for high-voltage drive readiness, and guards door latch states.
   - Decoupled Use Cases for vehicle telemetry updates, automated simulation, media playback, and persistent settings.

3. **Data Layer (`data/*`)**:
   - `SimulatedVehicleDataSource`: Thread-safe `MutableStateFlow` simulation loop bridging Kotlin coroutines with the Java `VehicleSignalManager`.
   - `MediaRepositoryImpl`: Production integration with AndroidX Media3 `ExoPlayer` playing bundled local audio assets (`res/raw/demo_track.wav`).
   - `DataStoreSettingsDataSource`: Reactive local persistence using AndroidX Preferences DataStore.

4. **Framework & Interop Layer (`framework/*`)**:
   - `VehicleSignalManager.java`: Demonstrates seamless Java/Kotlin interoperability and signal abstraction.
   - `VehicleMonitorService`: Android background service monitoring vehicle signals and safety thresholds.
   - `StartupPerformanceManager`: Cold-launch milestone recording from Application init to first Compose frame.
   - `LifecycleTracker`: Real-time ring buffer tracking recent Android component lifecycle transitions.
   - `DeviceInfoProvider` & `NetworkMonitor`: Direct integration with Android system managers (`ActivityManager`, `BatteryManager`, `ConnectivityManager`).

---

## Application Navigation Flow

```text
Dashboard (Speedometer, Powertrain, Cabin, Quick Media Pill)
   |
   +---> Vehicle Simulator (Telemetry Sliders, Access Switches, Rule Validation)
   |
   +---> Media Player (Media3 ExoPlayer, Seekbar, Volume, Controls)
   |
   +---> Navigation Simulator (Maneuver Cards, Destinations: Home/Office/Airport, Map Canvas)
   |
   +---> Settings (DataStore Persistence: Dark Mode, Simulation Mode, Audio Volume)
   |
   +---> System Diagnostics (Live OS Info, RAM, Battery, Latency, 50-Event Lifecycle Log)
```

---

## Key Features

- **Automotive Dashboard**: High-visibility digital speedometer gauge, live battery state of charge (SoC), estimated EV range, cabin climate, and powertrain readiness indicators.
- **Vehicle Signal Simulator**: Interactive sliders and toggles to simulate driving dynamics. Enforces strict safety rules (e.g. vehicle cannot be READY if driver door is open or parking brake is set).
- **Media3 Playback**: Real local playback engine using ExoPlayer with track position polling, duration calculation, and transport controls.
- **Simulated Navigation**: Turn-by-turn guidance instruction cards, ETA calculation, and route canvas visualization.
- **Settings Persistence**: User preferences survive application restarts through asynchronous Preferences DataStore.
- **Diagnostics Screen**: Real-time inspection of OS version, device manufacturer, RAM usage, connectivity status, and cold startup duration.

---

## Build & Test Instructions

### Build Debug APK
```bash
gradle assembleDebug
```

### Run Unit & Robolectric Tests
```bash
gradle testDebugUnitTest
```

### Run Python Startup Analyzer
```bash
python3 scripts/startup_analyzer.py startup.log
```

---

## Project Documentation Index

- [Architecture Guide](docs/architecture.md)
- [Automotive & Vehicle Architecture](docs/automotive-architecture.md)
- [Media & Audio Architecture](docs/audio-architecture.md)
- [Testing Strategy & Coverage](docs/testing-strategy.md)
- [Performance & Startup Milestones](docs/performance.md)
- [AOSP & Android Automotive OS Overview](docs/aosp-overview.md)
- [Coding Guidelines & Standards](docs/coding-guidelines.md)
- [Agile & Sprint Delivery Plan](docs/agile-plan.md)
