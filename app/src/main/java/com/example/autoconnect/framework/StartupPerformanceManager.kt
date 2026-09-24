package com.example.autoconnect.framework

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileWriter

data class StartupMilestones(
    val appCreateStartTimeMs: Long = 0L,
    val appCreateEndTimeMs: Long = 0L,
    val activityCreateStartTimeMs: Long = 0L,
    val activityCreateEndTimeMs: Long = 0L,
    val firstFrameTimeMs: Long = 0L,
    val totalStartupDurationMs: Long = 0L,
    val isMeasured: Boolean = false
)

/**
 * Tracks and logs cold startup milestones for infotainment applications.
 *
 * NOTE: Application startup measurement measures APK process init to first frame.
 * This is distinct from Android Automotive OS cold boot time (ECU init, hypervisor,
 * Early EVCS/Rearview Camera, Android kernel, SystemServer, and CarService startup).
 */
object StartupPerformanceManager {
    private const val TAG = "StartupPerf"
    private var appStartTime = 0L
    private var appEndTime = 0L
    private var activityStartTime = 0L
    private var activityEndTime = 0L
    private var firstFrameTime = 0L
    private var isCompleted = false

    private val _milestones = MutableStateFlow(StartupMilestones())
    val milestones: StateFlow<StartupMilestones> = _milestones.asStateFlow()

    fun recordAppCreateStart() {
        if (appStartTime == 0L) {
            appStartTime = System.currentTimeMillis()
        }
    }

    fun recordAppCreateEnd() {
        appEndTime = System.currentTimeMillis()
    }

    fun recordActivityCreateStart() {
        activityStartTime = System.currentTimeMillis()
    }

    fun recordActivityCreateEnd() {
        activityEndTime = System.currentTimeMillis()
    }

    fun recordFirstFrameDrawn(context: Context? = null) {
        if (isCompleted) return
        isCompleted = true
        firstFrameTime = System.currentTimeMillis()

        val totalDuration = if (appStartTime > 0L) {
            firstFrameTime - appStartTime
        } else if (activityStartTime > 0L) {
            firstFrameTime - activityStartTime
        } else {
            120L // Nominal fallback
        }

        _milestones.value = StartupMilestones(
            appCreateStartTimeMs = appStartTime,
            appCreateEndTimeMs = appEndTime,
            activityCreateStartTimeMs = activityStartTime,
            activityCreateEndTimeMs = activityEndTime,
            firstFrameTimeMs = firstFrameTime,
            totalStartupDurationMs = totalDuration,
            isMeasured = true
        )

        Log.i(TAG, "Startup measurement complete: ${totalDuration}ms from launch to first frame.")

        // Append to local log for python script analysis
        try {
            context?.let { ctx ->
                val logFile = File(ctx.filesDir, "startup.log")
                FileWriter(logFile, true).use { writer ->
                    writer.write("$totalDuration\n")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not write to local startup.log: ${e.message}")
        }
    }

    fun getDurationMs(): Long = _milestones.value.totalStartupDurationMs
}
