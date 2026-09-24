package com.example

import com.example.autoconnect.domain.vehicle.ValidateVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.VehicleState
import com.example.autoconnect.framework.VehicleSignalManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    private val validateUseCase = ValidateVehicleStateUseCase()

    @Test
    fun `valid vehicle state passes validation`() {
        val validState = VehicleState(
            speed = 50,
            battery = 80,
            temperature = 22,
            range = 300,
            engineRunning = true,
            vehicleReady = true,
            driverDoorOpen = false,
            passengerDoorOpen = false,
            parkingBrake = false
        )
        val result = validateUseCase(validState)
        assertTrue(result.isValid)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `vehicle cannot be ready when driver door is open`() {
        val invalidState = VehicleState(
            speed = 0,
            battery = 80,
            vehicleReady = true,
            driverDoorOpen = true
        )
        val result = validateUseCase(invalidState)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("driver door", ignoreCase = true) })
    }

    @Test
    fun `engine cannot run with parking brake engaged`() {
        val invalidState = VehicleState(
            engineRunning = true,
            parkingBrake = true
        )
        val result = validateUseCase(invalidState)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("parking brake", ignoreCase = true) })
    }

    @Test
    fun `vehicle cannot be ready with critically low battery`() {
        val lowBatteryState = VehicleState(
            battery = 5,
            vehicleReady = true,
            engineRunning = false
        )
        val result = validateUseCase(lowBatteryState)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("battery", ignoreCase = true) })
    }

    @Test
    fun `vehicle signal manager java interop works accurately`() {
        val manager = VehicleSignalManager.getInstance()
        manager.setSpeed(75)
        manager.setBattery(82)
        manager.setEngineRunning(true)
        manager.setVehicleReady(true)

        assertEquals(75, manager.getSpeed())
        assertEquals(82, manager.getBattery())
        assertTrue(manager.isEngineRunning())
        assertTrue(manager.isVehicleReady())
    }
}
