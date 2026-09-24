# Automotive Performance & Startup Latency

## Cold Launch Optimization

Automotive HMI systems have stringent latency requirements. When an automobile ignition is turned on, the cluster and core infotainment functions must be interactive within target time windows.

### Startup Milestones Recorded by `StartupPerformanceManager`

1. **Application.onCreate Start (`t0`)**: Process entry point.
2. **Application.onCreate End (`t1`)**: DI container & framework service registration.
3. **MainActivity.onCreate Start (`t2`)**: Activity window initialization.
4. **MainActivity.onCreate End (`t3`)**: Edge-to-edge layout & compose tree attachment.
5. **First Frame Drawn (`t4`)**: UI renders to the screen and is interactive.

$$\text{Total App Cold Launch Latency} = t_4 - t_0$$

### Offline Analysis with `startup_analyzer.py`

Samples are written to `startup.log` upon initial layout completion. The analyzer script computes:
- Sample Count
- Minimum Latency
- Maximum Latency
- Average Latency
