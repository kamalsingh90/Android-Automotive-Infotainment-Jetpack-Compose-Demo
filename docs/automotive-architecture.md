# Android Automotive OS Architecture

## AOSP Automotive Stack vs. AutoConnect Simulation

In a production Android Automotive OS (AAOS) deployment, software interacts through multiple deep OS layers down to physical Electronic Control Units (ECUs) and vehicle networks (CAN, LIN, Automotive Ethernet).

```text
+-----------------------------------------------------------+
|              Automotive Applications                      |
|         (OEM HMI, Media, Navigation, AutoConnect)         |
+-----------------------------+-----------------------------+
                              |
                              v
+-----------------------------------------------------------+
|               Car API (android.car.*)                     |
|         (CarPropertyManager, CarHvacManager, etc.)        |
+-----------------------------+-----------------------------+
                              |
                              v
+-----------------------------------------------------------+
|               Car Service (car-service.jar)               |
|         (Runs in system_server / CarServiceHelper)        |
+-----------------------------+-----------------------------+
                              |
                              v
+-----------------------------------------------------------+
|               Vehicle HAL (VHAL)                          |
|         (AIDL/HIDL interface: IVehicle.aidl)              |
+-----------------------------+-----------------------------+
                              |
                              v
+-----------------------------------------------------------+
|              Microcontroller / CAN Controller             |
|         (CAN Bus, FlexRay, Automotive Ethernet)           |
+-----------------------------+-----------------------------+
                              |
                              v
+-----------------------------------------------------------+
|                    Physical ECUs                          |
|         (Powertrain, BCM, Battery Management BMS)         |
+-----------------------------------------------------------+
```

## AutoConnect Architectural Bridge

Because standard Android developer devices and streaming test sandboxes lack real vehicle buses and VHAL hardware nodes:

1. **Abstraction Boundary**: `VehicleDataSource` and `VehicleSignalManager.java` act as an abstraction proxy mimicking VHAL `IVehicle` properties.
2. **Java Interop**: `VehicleSignalManager.java` demonstrates how legacy C++/Java hardware wrappers interface cleanly with modern Kotlin coroutine Flows.
3. **Simulation Boundary**:
   > Vehicle signals are simulated for demonstration purposes. This project does not implement production vehicle control, ECU communication, CAN communication, Vehicle HAL, or safety-critical automotive functionality.
