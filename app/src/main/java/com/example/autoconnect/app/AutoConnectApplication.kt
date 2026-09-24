package com.example.autoconnect.app

import android.app.Application
import android.content.Intent
import com.example.autoconnect.framework.LifecycleTracker
import com.example.autoconnect.framework.StartupPerformanceManager
import com.example.autoconnect.framework.VehicleMonitorService

/**
 * Application class for AutoConnect Automotive Infotainment.
 * Initializes dependency injection container, tracks cold startup performance,
 * and boots core automotive monitoring services.
 */
class AutoConnectApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        StartupPerformanceManager.recordAppCreateStart()
        super.onCreate()

        LifecycleTracker.track("APP_ON_CREATE", "AutoConnectApplication")

        appContainer = AppModule.provideAppContainer(applicationContext)

        // Boot Vehicle Monitoring Service
        try {
            val serviceIntent = Intent(this, VehicleMonitorService::class.java).apply {
                action = VehicleMonitorService.ACTION_START_MONITORING
            }
            startService(serviceIntent)
        } catch (_: Exception) {
            // Handled gracefully in testing/restricted sandbox environments
        }

        StartupPerformanceManager.recordAppCreateEnd()
    }
}
