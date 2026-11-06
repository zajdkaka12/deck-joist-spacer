package com.deckjoist.spacer.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.core.theme.CedarDeep
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.domain.model.JoistLayout
import kotlin.math.abs
import kotlin.math.min

internal class PlanMap(
    val left: Float,
    val top: Float,
    val planWidth: Float,
    val planHeight: Float,
    val pxPerMm: Float
)

internal fun planMap(
    canvasWidth: Float,
    canvasHeight: Float,
    runMm: Float,
    spanMm: Float,
    padPx: Float,
    zoom: Float,
    offset: Offset
): PlanMap {
    val fit = min(
        (canvasWidth - padPx * 2f) / runMm.coerceAtLeast(1f),
        (canvasHeight - padPx * 2f) / spanMm.coerceAtLeast(1f)
    ).coerceAtLeast(0.0001f)
    val scaled = fit * zoom
    val planWidth = runMm * scaled
    val planHeight = spanMm * scaled
    return PlanMap(
        left = (canvasWidth - planWidth) / 2f + offset.x,
        top = (canvasHeight - planHeight) / 2f + offset.y,
        planWidth = planWidth,
        planHeight = planHeight,
        pxPerMm = scaled
    )
}

@Composable
fun FramingCanvas(
    layout: JoistLayout,
    progress: Float,
    modifier: Modifier = Modifier,
    highlight: Int = -1,
    zoom: Float = 1f,
    offset: Offset = Offset.Zero,
    interactive: Boolean = false,
    showGrid: Boolean = true,
    onJoistTap: (Int) -> Unit = { },
    onTransform: (Offset, Float) -> Unit = { _, _ -> },
    onReset: () -> Unit = { }
) {
    val base = modifier.fillMaxSize()
    val wired = if (interactive) {
        base
            .pointerInput(layout) {
                detectTransformGestures { _, pan, gestureZoom, _ ->
                    onTransform(pan, gestureZoom)
                }
            }
            .pointerInput(layout, zoom, offset) {
                detectTapGestures(
                    onDoubleTap = { onReset() },
                    onTap = { point ->
                        val map = planMap(
                            size.width.toFloat(),
                            size.height.toFloat(),
                            layout.runMm.toFloat(),
                            layout.spanMm.toFloat(),
                            16.dp.toPx(),
                            zoom,
                            offset
                        )
                        val tolerance = 28.dp.toPx()
                        var best = -1
                        var bestDistance = tolerance
                        layout.marksMm.forEachIndexed { index, mark ->
                            val distance = abs(point.x - (map.left + mark * map.pxPerMm))
                            if (distance <= bestDistance) {
                                bestDistance = distance
                                best = index
                            }
                        }
                        if (best >= 0) onJoistTap(best)
                    }
                )
            }
    } else {
        base
    }

    Canvas(modifier = wired) {
        val padPx = 16.dp.toPx()
        val map = planMap(
            size.width,
            size.height,
            layout.runMm.toFloat(),
            layout.spanMm.toFloat(),
            padPx,
            zoom,
            offset
        )
        val corner = CornerRadius(4.dp.toPx())

        drawRoundRect(
            color = PaperSurface,
            topLeft = Offset(map.left, map.top),
            size = Size(map.planWidth, map.planHeight),
            cornerRadius = corner
        )

        if (showGrid) {
            val step = GameConfig.GRID_PITCH_MM * map.pxPerMm
            if (step > 6f) {
                var x = map.left + step
                while (x < map.left + map.planWidth) {
                    drawLine(
                        color = SecondarySage.copy(alpha = 0.7f),
                        start = Offset(x, map.top),
                        end = Offset(x, map.top + map.planHeight),
                        strokeWidth = 1f
                    )
                    x += step
                }
                var y = map.top + step
                while (y < map.top + map.planHeight) {
                    drawLine(
                        color = SecondarySage.copy(alpha = 0.7f),
                        start = Offset(map.left, y),
                        end = Offset(map.left + map.planWidth, y),
                        strokeWidth = 1f
                    )
                    y += step
                }
            }
        }

        val beamStroke = 6.dp.toPx()
        val beamLines = layout.beamLines.coerceAtLeast(2)
        for (index in 0 until beamLines) {
            val fraction = index.toFloat() / (beamLines - 1).toFloat()
            val y = (map.top + map.planHeight * fraction)
                .coerceIn(map.top + beamStroke / 2f, map.top + map.planHeight - beamStroke / 2f)
            drawLine(
                color = CedarDeep.copy(alpha = 0.55f),
                start = Offset(map.left, y),
                end = Offset(map.left + map.planWidth, y),
                strokeWidth = beamStroke
            )
        }

        val total = layout.marksMm.size.coerceAtLeast(1)
        val thickness = (layout.joistWidthMm * map.pxPerMm).coerceAtLeast(2.dp.toPx())
        val slide = 24.dp.toPx()
        val tickLength = 9.dp.toPx()

        layout.marksMm.forEachIndexed { index, mark ->
            val local = ((progress * total) - index).coerceIn(0f, 1f)
            if (local > 0f) {
                val x = map.left + mark * map.pxPerMm - slide * (1f - local)
                val selected = index == highlight
                drawRect(
                    color = (if (selected) RustAccent else CedarPrimary).copy(alpha = local),
                    topLeft = Offset(x, map.top),
                    size = Size(thickness, map.planHeight)
                )
                drawLine(
                    color = RustAccent.copy(alpha = local),
                    start = Offset(x + thickness / 2f, map.top - tickLength),
                    end = Offset(x + thickness / 2f, map.top + tickLength),
                    strokeWidth = 2.dp.toPx()
                )
                if (selected) {
                    drawCircle(
                        color = RustAccent,
                        radius = 5.dp.toPx(),
                        center = Offset(x + thickness / 2f, map.top - tickLength)
                    )
                }
            }
        }

        drawRoundRect(
            color = CedarPrimary,
            topLeft = Offset(map.left, map.top),
            size = Size(map.planWidth, map.planHeight),
            cornerRadius = corner,
            style = Stroke(width = 5.dp.toPx())
        )
    }
}
