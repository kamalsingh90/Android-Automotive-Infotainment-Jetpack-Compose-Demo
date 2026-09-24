# Agile Delivery Plan & Sprint Roadmap

## Sprint 1: Architecture, Core Models & Simulation Scaffolding
- [x] Establish Clean Architecture folder structure (`domain`, `data`, `framework`, `feature`, `core`).
- [x] Configure Gradle version catalog (`Media3`, `DataStore`, `Navigation Compose`).
- [x] Implement Java/Kotlin interop signal manager (`VehicleSignalManager.java`).
- [x] Implement `SimulatedVehicleDataSource` with background coroutine simulation loop.
- [x] Implement cold-launch startup tracking and ring buffer lifecycle tracking.

## Sprint 2: Core Infotainment UI & Design System
- [x] Build Automotive Material 3 Dark theme and typography.
- [x] Develop custom `VehicleSpeedGauge` Canvas component with tick markers and glow needle.
- [x] Develop `DashboardScreen` with live battery, range, and powertrain cards.
- [x] Develop `VehicleSimulatorScreen` with analog sliders and safety rule validation.

## Sprint 3: Media3, Navigation & Persistent Settings
- [x] Integrate Media3 ExoPlayer with bundled audio track.
- [x] Build `MediaScreen` with timeline slider and transport controls.
- [x] Build `NavigationSimulatorScreen` with simulated route canvas and preset destinations.
- [x] Implement Preferences DataStore settings persistence.

## Sprint 4: Diagnostics, Quality Assurance & CI/CD
- [x] Build `DiagnosticsScreen` with real hardware info and lifecycle event log.
- [x] Write Python `scripts/startup_analyzer.py` tool.
- [x] Configure `.gitlab-ci.yml` pipeline stages.
- [x] Implement unit tests and Robolectric tests for all CUJs.
