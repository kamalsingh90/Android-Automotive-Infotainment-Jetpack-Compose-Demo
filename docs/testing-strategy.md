# Testing Strategy & Verification Plan

## Test Pyramid

```text
               / \
              /   \
             / UI  \        <-- Robolectric Compose Tests (CUJs)
            /-------\
           / Service \      <-- Background Service & Interop Tests
          /-----------\
         / Unit & Rules\    <-- UseCase Validation & State Flow Tests
        +---------------+
```

### 1. Domain Unit Tests (JVM)
- Tests vehicle rule validation (e.g., driver door open prevents READY state; parking brake prevents engine start).
- Tests numerical boundary clamping (speed, battery, range, temperature).
- Tests settings serialization and fallback defaults.

### 2. Robolectric Tests (CUJs)
- Validates Compose UI rendering without physical Android emulator devices.
- Asserts presence of `testTag` identifiers across:
  - `dashboard_screen`, `speed_gauge`, `digital_speed_text`
  - `vehicle_simulator_screen`, `slider_speed`, `btn_apply_simulator`
  - `media_screen`, `btn_play_pause`, `slider_seek`
  - `navigation_screen`, `dest_home`, `dest_office`, `dest_airport`
  - `settings_screen`, `switch_dark_mode`, `switch_simulation`
  - `diagnostics_screen`, `diag_vehicle_service`, `diag_startup`

### 3. CI/CD Automated Execution
Tests run automatically on every pipeline push via `gradle testDebugUnitTest`.
