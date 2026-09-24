package com.example.autoconnect.app

import android.content.Context
import com.example.autoconnect.data.media.MediaRepositoryImpl
import com.example.autoconnect.data.settings.DataStoreSettingsDataSource
import com.example.autoconnect.data.settings.SettingsRepositoryImpl
import com.example.autoconnect.data.vehicle.SimulatedVehicleDataSource
import com.example.autoconnect.data.vehicle.VehicleDataSource
import com.example.autoconnect.data.vehicle.VehicleRepositoryImpl
import com.example.autoconnect.domain.media.GetMediaStateUseCase
import com.example.autoconnect.domain.media.MediaRepository
import com.example.autoconnect.domain.media.PauseMediaUseCase
import com.example.autoconnect.domain.media.PlayMediaUseCase
import com.example.autoconnect.domain.media.SeekMediaUseCase
import com.example.autoconnect.domain.media.SetVolumeUseCase
import com.example.autoconnect.domain.media.SkipMediaUseCase
import com.example.autoconnect.domain.media.TogglePlayPauseUseCase
import com.example.autoconnect.domain.settings.GetSettingsUseCase
import com.example.autoconnect.domain.settings.SettingsRepository
import com.example.autoconnect.domain.settings.UpdateSettingsUseCase
import com.example.autoconnect.domain.vehicle.GetVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.StartVehicleSimulationUseCase
import com.example.autoconnect.domain.vehicle.StopVehicleSimulationUseCase
import com.example.autoconnect.domain.vehicle.UpdateVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.ValidateVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.VehicleRepository
import com.example.autoconnect.framework.DeviceInfoProvider
import com.example.autoconnect.framework.NetworkMonitor
import com.example.autoconnect.framework.VehicleSignalManager

/**
 * Dependency Injection container providing application-scoped singleton dependencies.
 */
interface AppContainer {
    val vehicleSignalManager: VehicleSignalManager
    val vehicleDataSource: VehicleDataSource
    val vehicleRepository: VehicleRepository

    val mediaRepository: MediaRepository

    val settingsRepository: SettingsRepository

    val networkMonitor: NetworkMonitor
    val deviceInfoProvider: DeviceInfoProvider

    // Vehicle Use Cases
    val validateVehicleStateUseCase: ValidateVehicleStateUseCase
    val getVehicleStateUseCase: GetVehicleStateUseCase
    val updateVehicleStateUseCase: UpdateVehicleStateUseCase
    val startVehicleSimulationUseCase: StartVehicleSimulationUseCase
    val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase

    // Media Use Cases
    val getMediaStateUseCase: GetMediaStateUseCase
    val playMediaUseCase: PlayMediaUseCase
    val pauseMediaUseCase: PauseMediaUseCase
    val togglePlayPauseUseCase: TogglePlayPauseUseCase
    val skipMediaUseCase: SkipMediaUseCase
    val seekMediaUseCase: SeekMediaUseCase
    val setVolumeUseCase: SetVolumeUseCase

    // Settings Use Cases
    val getSettingsUseCase: GetSettingsUseCase
    val updateSettingsUseCase: UpdateSettingsUseCase
}

/**
 * Default production implementation of AppContainer.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    override val vehicleSignalManager: VehicleSignalManager by lazy {
        VehicleSignalManager()
    }

    override val vehicleDataSource: VehicleDataSource by lazy {
        SimulatedVehicleDataSource(signalManager = vehicleSignalManager)
    }

    override val vehicleRepository: VehicleRepository by lazy {
        VehicleRepositoryImpl(dataSource = vehicleDataSource)
    }

    override val mediaRepository: MediaRepository by lazy {
        MediaRepositoryImpl(context = context)
    }

    private val settingsDataSource: DataStoreSettingsDataSource by lazy {
        DataStoreSettingsDataSource(context = context)
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(dataSource = settingsDataSource)
    }

    override val networkMonitor: NetworkMonitor by lazy {
        NetworkMonitor(context = context)
    }

    override val deviceInfoProvider: DeviceInfoProvider by lazy {
        DeviceInfoProvider(context = context)
    }

    override val validateVehicleStateUseCase: ValidateVehicleStateUseCase by lazy {
        ValidateVehicleStateUseCase()
    }

    override val getVehicleStateUseCase: GetVehicleStateUseCase by lazy {
        GetVehicleStateUseCase(repository = vehicleRepository)
    }

    override val updateVehicleStateUseCase: UpdateVehicleStateUseCase by lazy {
        UpdateVehicleStateUseCase(
            repository = vehicleRepository,
            validator = validateVehicleStateUseCase
        )
    }

    override val startVehicleSimulationUseCase: StartVehicleSimulationUseCase by lazy {
        StartVehicleSimulationUseCase(repository = vehicleRepository)
    }

    override val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase by lazy {
        StopVehicleSimulationUseCase(repository = vehicleRepository)
    }

    override val getMediaStateUseCase: GetMediaStateUseCase by lazy {
        GetMediaStateUseCase(repository = mediaRepository)
    }

    override val playMediaUseCase: PlayMediaUseCase by lazy {
        PlayMediaUseCase(repository = mediaRepository)
    }

    override val pauseMediaUseCase: PauseMediaUseCase by lazy {
        PauseMediaUseCase(repository = mediaRepository)
    }

    override val togglePlayPauseUseCase: TogglePlayPauseUseCase by lazy {
        TogglePlayPauseUseCase(repository = mediaRepository)
    }

    override val skipMediaUseCase: SkipMediaUseCase by lazy {
        SkipMediaUseCase(repository = mediaRepository)
    }

    override val seekMediaUseCase: SeekMediaUseCase by lazy {
        SeekMediaUseCase(repository = mediaRepository)
    }

    override val setVolumeUseCase: SetVolumeUseCase by lazy {
        SetVolumeUseCase(repository = mediaRepository)
    }

    override val getSettingsUseCase: GetSettingsUseCase by lazy {
        GetSettingsUseCase(repository = settingsRepository)
    }

    override val updateSettingsUseCase: UpdateSettingsUseCase by lazy {
        UpdateSettingsUseCase(repository = settingsRepository)
    }
}
