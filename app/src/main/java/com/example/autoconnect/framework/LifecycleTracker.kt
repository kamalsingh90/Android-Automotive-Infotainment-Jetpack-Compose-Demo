package com.example.autoconnect.framework

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentLinkedDeque

data class LifecycleEvent(
    val id: Long,
    val event: String,
    val component: String,
    val timestamp: Long,
    val formattedTime: String
)

/**
 * Tracks Android lifecycle transitions across Activity and Application components.
 * Retains up to 50 most recent events for Diagnostics inspection.
 */
object LifecycleTracker {
    private const val MAX_EVENTS = 50
    private var eventCounter = 0L
    private val buffer = ConcurrentLinkedDeque<LifecycleEvent>()
    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    private val _eventsFlow = MutableStateFlow<List<LifecycleEvent>>(emptyList())
    val eventsFlow: StateFlow<List<LifecycleEvent>> = _eventsFlow.asStateFlow()

    private val _currentEvent = MutableStateFlow("INITIALIZED")
    val currentEvent: StateFlow<String> = _currentEvent.asStateFlow()

    @Synchronized
    fun track(event: Lifecycle.Event, component: String) {
        track(event.name, component)
    }

    @Synchronized
    fun track(eventName: String, component: String) {
        val now = System.currentTimeMillis()
        val entry = LifecycleEvent(
            id = ++eventCounter,
            event = eventName,
            component = component,
            timestamp = now,
            formattedTime = timeFormat.format(Date(now))
        )
        buffer.addFirst(entry)
        while (buffer.size > MAX_EVENTS) {
            buffer.removeLast()
        }
        _currentEvent.value = eventName
        _eventsFlow.value = buffer.toList()
    }

    fun getRecentEvents(): List<LifecycleEvent> = buffer.toList()
}
