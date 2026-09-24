# AutoConnect Coding Standards & Guidelines

## 1. Kotlin & Coroutines
- Use Kotlin as the primary language.
- Use explicit types on public API boundaries and Use Cases.
- Manage coroutine cancellations strictly using `Job` or `viewModelScope`.
- Use `StateFlow` and `SharedFlow` instead of LiveData.

## 2. Jetpack Compose
- Adhere to Material 3 (M3) theming.
- Keep Composables stateless where feasible; hoist state to ViewModels.
- Always attach unique `Modifier.testTag("...")` to interactive elements and telemetry readouts.
- Guarantee touch target dimensions of at least 48dp x 48dp.

## 3. Java Interoperability
- Java classes (e.g. `VehicleSignalManager.java`) must use clear thread synchronization (`synchronized` blocks) and encapsulate internal mutable fields.

## 4. Architecture
- Dependencies must point inward toward the Domain layer.
- Presentation never calls Data sources directly; always pass through Use Cases.
