# Android Automotive OS (AAOS) & AOSP Architectural Overview

## Android in Automotive Context

Unlike Android Auto (which is projected from a phone onto the car display via USB or Wi-Fi), **Android Automotive OS (AAOS)** is a full operating system running natively on the vehicle's in-vehicle infotainment (IVI) compute hardware.

### Key Automotive Subsystems in AOSP:

1. **CarService**:
   Central orchestrator running inside Android system server. Hosts `CarPropertyService`, `CarAudioService`, `CarHvacService`, and `CarDrivingStateService`.

2. **Vehicle HAL (VHAL)**:
   The hardware abstraction layer defining typed vehicle properties (e.g. `PERF_VEHICLE_SPEED`, `HVAC_TEMPERATURE_SET`).

3. **Driver Distraction Guidelines (DDG)**:
   AAOS enforces UX restriction rules: while the vehicle is in gear or traveling above 0 km/h, video playback and deep multi-level menus are blocked or simplified.

4. **Multi-Display & Power Management**:
   AAOS supports multiple physical display outputs (Instrument Cluster, Center Information Display, Passenger Screen) with rapid suspend-to-RAM (`Garage Mode`) for over-the-air updates.
