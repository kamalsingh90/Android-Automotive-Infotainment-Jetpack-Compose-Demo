package com.example.autoconnect.framework;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Java automotive vehicle signal framework component demonstrating Java/Kotlin interoperability.
 *
 * Architecture:
 * Kotlin -> VehicleDataSource -> VehicleSignalManager.java -> SignalProvider
 *
 * NOTE: Vehicle signals are simulated for demonstration purposes. This project does
 * not implement production vehicle control, ECU communication, CAN communication,
 * Vehicle HAL, or safety-critical automotive functionality.
 */
public class VehicleSignalManager {

    /**
     * Hardware Abstraction Layer / Provider contract for vehicle signals.
     * Can be backed by CAN bus bridge, Vehicle HAL, or simulation.
     */
    public interface SignalProvider {
        int readSpeed();
        int readBatteryLevel();
        int readCabinTemperature();
        int readEstimatedRange();
        boolean readEngineRunning();
        boolean readVehicleReady();
        boolean readDriverDoorOpen();
        boolean readPassengerDoorOpen();
        boolean readParkingBrake();
        void writeSpeed(int speed);
        void writeBatteryLevel(int battery);
        void writeCabinTemperature(int temp);
        void writeEstimatedRange(int range);
        void writeEngineRunning(boolean running);
        void writeVehicleReady(boolean ready);
        void writeDriverDoorOpen(boolean open);
        void writePassengerDoorOpen(boolean open);
        void writeParkingBrake(boolean engaged);
    }

    public interface SignalChangeListener {
        void onSignalChanged(String signalName, Object newValue);
    }

    /**
     * Default in-memory simulated signal provider.
     */
    public static class SimulatedSignalProvider implements SignalProvider {
        private volatile int speed = 42;
        private volatile int battery = 78;
        private volatile int temperature = 24;
        private volatile int range = 320;
        private volatile boolean engineRunning = true;
        private volatile boolean vehicleReady = true;
        private volatile boolean driverDoorOpen = false;
        private volatile boolean passengerDoorOpen = false;
        private volatile boolean parkingBrake = false;

        @Override public int readSpeed() { return speed; }
        @Override public int readBatteryLevel() { return battery; }
        @Override public int readCabinTemperature() { return temperature; }
        @Override public int readEstimatedRange() { return range; }
        @Override public boolean readEngineRunning() { return engineRunning; }
        @Override public boolean readVehicleReady() { return vehicleReady; }
        @Override public boolean readDriverDoorOpen() { return driverDoorOpen; }
        @Override public boolean readPassengerDoorOpen() { return passengerDoorOpen; }
        @Override public boolean readParkingBrake() { return parkingBrake; }

        @Override public void writeSpeed(int speed) { this.speed = speed; }
        @Override public void writeBatteryLevel(int battery) { this.battery = battery; }
        @Override public void writeCabinTemperature(int temp) { this.temperature = temp; }
        @Override public void writeEstimatedRange(int range) { this.range = range; }
        @Override public void writeEngineRunning(boolean running) { this.engineRunning = running; }
        @Override public void writeVehicleReady(boolean ready) { this.vehicleReady = ready; }
        @Override public void writeDriverDoorOpen(boolean open) { this.driverDoorOpen = open; }
        @Override public void writePassengerDoorOpen(boolean open) { this.passengerDoorOpen = open; }
        @Override public void writeParkingBrake(boolean engaged) { this.parkingBrake = engaged; }
    }

    private static volatile VehicleSignalManager instance;

    public static VehicleSignalManager getInstance() {
        if (instance == null) {
            synchronized (VehicleSignalManager.class) {
                if (instance == null) {
                    instance = new VehicleSignalManager();
                }
            }
        }
        return instance;
    }

    private SignalProvider provider;
    private final List<SignalChangeListener> listeners = new CopyOnWriteArrayList<>();

    public VehicleSignalManager() {
        this(new SimulatedSignalProvider());
    }

    public VehicleSignalManager(SignalProvider provider) {
        this.provider = provider != null ? provider : new SimulatedSignalProvider();
    }

    public synchronized void setSignalProvider(SignalProvider newProvider) {
        if (newProvider != null) {
            this.provider = newProvider;
        }
    }

    public SignalProvider getSignalProvider() {
        return provider;
    }

    public void addListener(SignalChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(SignalChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifySignalChanged(String signalName, Object value) {
        for (SignalChangeListener listener : listeners) {
            listener.onSignalChanged(signalName, value);
        }
    }

    public int getSpeed() {
        return Math.max(0, Math.min(260, provider.readSpeed()));
    }

    public void setSpeed(int speed) {
        int clamped = Math.max(0, Math.min(260, speed));
        provider.writeSpeed(clamped);
        notifySignalChanged("speed", clamped);
    }

    public int getBatteryLevel() {
        return Math.max(0, Math.min(100, provider.readBatteryLevel()));
    }

    public int getBattery() {
        return getBatteryLevel();
    }

    public void setBatteryLevel(int battery) {
        int clamped = Math.max(0, Math.min(100, battery));
        provider.writeBatteryLevel(clamped);
        notifySignalChanged("battery", clamped);
    }

    public void setBattery(int battery) {
        setBatteryLevel(battery);
    }

    public int getCabinTemperature() {
        return Math.max(-20, Math.min(50, provider.readCabinTemperature()));
    }

    public void setCabinTemperature(int temp) {
        int clamped = Math.max(-20, Math.min(50, temp));
        provider.writeCabinTemperature(clamped);
        notifySignalChanged("temperature", clamped);
    }

    public int getEstimatedRange() {
        return Math.max(0, provider.readEstimatedRange());
    }

    public void setEstimatedRange(int range) {
        int clamped = Math.max(0, range);
        provider.writeEstimatedRange(clamped);
        notifySignalChanged("range", clamped);
    }

    public boolean isEngineRunning() {
        return provider.readEngineRunning();
    }

    public void setEngineRunning(boolean running) {
        provider.writeEngineRunning(running);
        notifySignalChanged("engineRunning", running);
    }

    public boolean isVehicleReady() {
        return provider.readVehicleReady();
    }

    public void setVehicleReady(boolean ready) {
        provider.writeVehicleReady(ready);
        notifySignalChanged("vehicleReady", ready);
    }

    public boolean isDriverDoorOpen() {
        return provider.readDriverDoorOpen();
    }

    public void setDriverDoorOpen(boolean open) {
        provider.writeDriverDoorOpen(open);
        notifySignalChanged("driverDoorOpen", open);
    }

    public boolean isPassengerDoorOpen() {
        return provider.readPassengerDoorOpen();
    }

    public void setPassengerDoorOpen(boolean open) {
        provider.writePassengerDoorOpen(open);
        notifySignalChanged("passengerDoorOpen", open);
    }

    public boolean isParkingBrakeEngaged() {
        return provider.readParkingBrake();
    }

    public void setParkingBrakeEngaged(boolean engaged) {
        provider.writeParkingBrake(engaged);
        notifySignalChanged("parkingBrake", engaged);
    }
}
