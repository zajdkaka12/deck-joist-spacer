package com.deckjoist.spacer.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.config.GameConfig
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.PaperOverlay
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.core.ui.CounterChip
import com.deckjoist.spacer.core.ui.FramingCanvas
import com.deckjoist.spacer.core.ui.PrimaryButton
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.ScreenHeader
import com.deckjoist.spacer.core.ui.SecondaryButton
import com.deckjoist.spacer.core.ui.deckSizeText
import com.deckjoist.spacer.core.ui.lengthText
import com.deckjoist.spacer.presentation.dialog.JoistMarkDialog

@Composable
fun GameScreen(
    onBack: () -> Unit,
    onMarks: () -> Unit,
    onBoards: () -> Unit,
    onFasteners: () -> Unit,
    onMaterialList: () -> Unit,
    onNotes: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    val report = state.report
    val haptics = LocalHapticFeedback.current

    var zoom by remember { mutableFloatStateOf(1f) }
    var pan by remember { mutableStateOf(Offset.Zero) }
    val fanOut = remember { Animatable(0f) }

    DisposableEffect(Unit) {
        gameViewModel.resume()
        onDispose { gameViewModel.pause() }
    }

    val joistCount = report?.joists?.joistCount ?: 1
    LaunchedEffect(state.revision, joistCount) {
        fanOut.snapTo(0f)
        val sweep = (GameConfig.FAN_OUT_STAGGER_MS * joistCount + GameConfig.FAN_OUT_PIECE_MS)
            .coerceAtMost(GameConfig.FAN_OUT_MAX_MS)
        fanOut.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = sweep, easing = FastOutSlowInEasing)
        )
    }

    val selected = state.selectedJoist
    if (report != null && selected != null && selected in report.joists.marksMm.indices) {
        JoistMarkDialog(
            joistNumber = selected + 1,
            markText = lengthText(report.joists.marksMm[selected], state.unitSystem),
            sectionText = stringResource(
                R.string.joist_section_option,
                report.joists.joistDepthMm
            ),
            lengthText = lengthText(report.joists.spanMm, state.unitSystem),
            onDismiss = { gameViewModel.dismissJoist() }
        )
    }

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.game_title),
                subtitle = if (report != null) {
                    deckSizeText(
                        report.plan.lengthMm,
                        report.plan.widthMm,
                        state.unitSystem
                    )
                } else {
                    null
                },
                onBack = onBack,
                trailing = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(onClick = onNotes),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_pause),
                            contentDescription = stringResource(R.string.cd_build_notes),
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (report != null) {
                    FramingCanvas(
                        layout = report.joists,
                        progress = fanOut.value,
                        highlight = state.highlight,
                        zoom = zoom,
                        offset = pan,
                        interactive = true,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 210.dp),
                        onJoistTap = { index ->
                            if (state.hapticEnabled) {
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            gameViewModel.selectJoist(index)
                        },
                        onTransform = { gesturePan, gestureZoom ->
                            zoom = (zoom * gestureZoom)
                                .coerceIn(GameConfig.MIN_ZOOM, GameConfig.MAX_ZOOM)
                            pan = Offset(pan.x + gesturePan.x, pan.y + gesturePan.y)
                        },
                        onReset = {
                            zoom = 1f
                            pan = Offset.Zero
                        }
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .shadow(8.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, SecondarySage, RoundedCornerShape(14.dp)),
                    color = PaperSurface,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                        Row(
                            modifier = Modifier.semantics {
                                liveRegion = LiveRegionMode.Polite
                            },
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CounterChip(
                                value = (report?.joists?.joistCount ?: 0).toString(),
                                label = stringResource(R.string.unit_joists)
                            )
                            CounterChip(
                                value = (report?.joists?.beamLines ?: 0).toString(),
                                label = stringResource(R.string.unit_beams)
                            )
                            CounterChip(
                                value = (report?.boards?.rows ?: 0).toString(),
                                label = stringResource(R.string.unit_rows)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.game_hint),
                            style = MaterialTheme.typography.labelSmall,
                            color = CedarPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SecondaryButton(
                                text = stringResource(R.string.marks),
                                modifier = Modifier.weight(1f),
                                onClick = onMarks
                            )
                            SecondaryButton(
                                text = stringResource(R.string.boards),
                                modifier = Modifier.weight(1f),
                                onClick = onBoards
                            )
                            SecondaryButton(
                                text = stringResource(R.string.fasteners),
                                modifier = Modifier.weight(1f),
                                onClick = onFasteners
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        PrimaryButton(
                            text = stringResource(R.string.material_list),
                            onClick = onMaterialList
                        )
                    }
                }
            }
        }
    }
}
