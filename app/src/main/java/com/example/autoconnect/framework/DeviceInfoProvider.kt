package com.example.autoconnect.framework

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

data class DeviceInfo(
    val androidVersion: String,
    val sdkVersion: Int,
    val manufacturer: String,
    val model: String,
    val totalMemoryMb: Long,
    val availableMemoryMb: Long,
    val batteryPercent: Int,
    val isCharging: Boolean
)

/**
 * Accesses system hardware and OS metrics via standard Android Framework APIs.
 */
class DeviceInfoProvider(private val context: Context) {

    fun getDeviceInfo(): DeviceInfo {
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        activityManager?.getMemoryInfo(memoryInfo)

        val totalMem = memoryInfo.totalMem / (1024 * 1024)
        val availMem = memoryInfo.availMem / (1024 * 1024)

        val batteryStatus: Intent? = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        val batteryPct = if (level >= 0 && scale > 0) {
            (level * 100 / scale)
        } else {
            // Fallback for emulator environment without battery hardware
            100
        }

        return DeviceInfo(
            androidVersion = Build.VERSION.RELEASE ?: "15",
            sdkVersion = Build.VERSION.SDK_INT,
            manufacturer = Build.MANUFACTURER?.replaceFirstChar { it.uppercase() } ?: "Android",
            model = Build.MODEL ?: "Automotive Display",
            totalMemoryMb = totalMem,
            availableMemoryMb = availMem,
            batteryPercent = batteryPct,
            isCharging = isCharging
        )
    }
}
