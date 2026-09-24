# Clean Architecture & Engineering Design

## System Design Philosophy

AutoConnect adopts **Clean Architecture** to ensure testability, maintainability, and strict decoupling between presentation components, core business validation logic, and hardware/data abstraction layers.

```text
       +-------------------------------------------------------+
       |                  Presentation Layer                   |
       |  - Jetpack Compose UI (Dashboard, Simulator, etc.)    |
       |  - ViewModels (StateFlow, Lifecycle-aware)            |
       +---------------------------+---------------------------+
                                   |
                                   v
       +-------------------------------------------------------+
       |                     Domain Layer                      |
       |  - Use Cases (GetVehicleState, ValidateVehicleState)  |
       |  - Domain Entities (VehicleState, MediaState)         |
       |  - Repository Interfaces                              |
       +---------------------------+---------------------------+
                                   |
                                   v
       +-------------------------------------------------------+
       |                      Data Layer                       |
       |  - Repository Implementations                         |
       |  - Data Sources (Simulated, DataStore, Media3)        |
       +---------------------------+---------------------------+
                                   |
                                   v
       +-------------------------------------------------------+
       |             Framework & Hardware Bridge               |
       |  - Android Services (VehicleMonitorService)           |
       |  - Java Bridge (VehicleSignalManager.java)            |
       |  - System Providers (Network, Battery, Memory)        |
       +-------------------------------------------------------+
```

## Unidirectional Data Flow (UDF)

All UI state flows in a single direction from ViewModels down to Composables through immutable `StateFlow<T>`. User actions (such as slider adjustments, playback controls, or destination selections) are dispatched as intention events to the ViewModel, which executes domain Use Cases.

```text
User Interaction (Slider Drag)
          |
          v
ViewModel.updateDraftSpeed(80)
          |
          v
ValidateVehicleStateUseCase(draft)
          |
          v
VehicleRepository.updateVehicleState(draft)
          |
          v
VehicleDataSource (MutableStateFlow.value = state)
          |
          v
VehicleSignalManager.java (speed = 80)
          |
          v
StateFlow Emission -> DashboardViewModel -> Compose Recomposition
```
