package com.example.autoconnect.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AutoDarkSurfaceElevated
import com.example.ui.theme.AutoPrimaryCyan
import com.example.ui.theme.AutoSecondaryElectric
import com.example.ui.theme.AutoTextMuted
import com.example.ui.theme.AutoTextPrimary
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-performance automotive speedometer gauge rendered with Canvas.
 * Arc spans 240 degrees from 150° to 390°.
 * Ticks placed at: 0, 40, 80, 120, 160, 200, 240.
 */
@Composable
fun VehicleSpeedGauge(
    speed: Int,
    maxSpeed: Int = 240,
    modifier: Modifier = Modifier
) {
    val clampedSpeed = speed.coerceIn(0, maxSpeed)
    val animatedSpeed by animateFloatAsState(
        targetValue = clampedSpeed.toFloat(),
        animationSpec = tween(durationMillis = 350),
        label = "speed_animation"
    )

    val speedFraction = (animatedSpeed / maxSpeed.toFloat()).coerceIn(0f, 1f)

    val speedMarks = remember { listOf(0, 40, 80, 120, 160, 200, 240) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(240.dp)
            .testTag("speed_gauge")
    ) {
        Canvas(modifier = Modifier.size(230.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 14.dp.toPx()
            val radius = (size.minDimension - strokeWidth * 2) / 2f

            val startAngle = 150f
            val totalSweep = 240f
            val activeSweep = totalSweep * speedFraction

            // Background gauge track arc
            drawArc(
                color = AutoDarkSurfaceElevated,
                startAngle = startAngle,
                sweepAngle = totalSweep,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active glowing speed arc
            if (activeSweep > 0.5f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        0.4f to AutoSecondaryElectric,
                        1.0f to AutoPrimaryCyan,
                        center = center
                    ),
                    startAngle = startAngle,
                    sweepAngle = activeSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Ticks and scale markers
            val tickRadiusOuter = radius - (strokeWidth / 2) - 8.dp.toPx()
            val tickRadiusInner = tickRadiusOuter - 10.dp.toPx()

            for (mark in speedMarks) {
                val markFraction = mark / maxSpeed.toFloat()
                val angleDeg = startAngle + (markFraction * totalSweep)
                val angleRad = Math.toRadians(angleDeg.toDouble())

                val startX = center.x + (tickRadiusInner * cos(angleRad)).toFloat()
                val startY = center.y + (tickRadiusInner * sin(angleRad)).toFloat()
                val endX = center.x + (tickRadiusOuter * cos(angleRad)).toFloat()
                val endY = center.y + (tickRadiusOuter * sin(angleRad)).toFloat()

                val isPassed = animatedSpeed >= mark
                drawLine(
                    color = if (isPassed) AutoPrimaryCyan else AutoTextMuted,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (mark % 80 == 0) 3.5.dp.toPx() else 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Needle indicator
            val needleAngleDeg = startAngle + activeSweep
            val needleRad = Math.toRadians(needleAngleDeg.toDouble())
            val needleLength = radius - 16.dp.toPx()
            val needleTipX = center.x + (needleLength * cos(needleRad)).toFloat()
            val needleTipY = center.y + (needleLength * sin(needleRad)).toFloat()

            drawLine(
                color = AutoPrimaryCyan,
                start = center,
                end = Offset(needleTipX, needleTipY),
                strokeWidth = 3.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Center needle pivot point
            drawCircle(
                color = AutoPrimaryCyan,
                radius = 6.dp.toPx(),
                center = center
            )
            drawCircle(
                color = Color.Black,
                radius = 3.dp.toPx(),
                center = center
            )
        }

        // Digital Speed Center Readout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "${clampedSpeed}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = AutoTextPrimary,
                modifier = Modifier.testTag("digital_speed_text")
            )
            Text(
                text = "km/h",
                style = MaterialTheme.typography.labelLarge,
                color = AutoSecondaryElectric,
                letterSpacing = 1.sp
            )
        }
    }
}
