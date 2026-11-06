package com.deckjoist.spacer.presentation.joist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.PaperOverlay
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.RustTint
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.core.ui.CounterChip
import com.deckjoist.spacer.core.ui.FramingCanvas
import com.deckjoist.spacer.core.ui.NoteCard
import com.deckjoist.spacer.core.ui.PrimaryButton
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.ScreenHeader
import com.deckjoist.spacer.core.ui.SectionHeader
import com.deckjoist.spacer.core.ui.SegmentedChips
import com.deckjoist.spacer.domain.model.UnitSystem
import com.deckjoist.spacer.domain.model.millimetresToInches

@Composable
private fun lengthOptionLabels(values: List<Int>, system: UnitSystem): List<String> {
    val labels = ArrayList<String>(values.size)
    for (value in values) {
        labels.add(
            if (system == UnitSystem.METRIC) {
                stringResource(R.string.len_mm, value)
            } else {
                stringResource(R.string.len_in, millimetresToInches(value))
            }
        )
    }
    return labels
}

@Composable
private fun millimetreLabels(values: List<Int>): List<String> {
    val labels = ArrayList<String>(values.size)
    for (value in values) {
        labels.add(stringResource(R.string.len_mm, value))
    }
    return labels
}

@Composable
private fun sectionLabels(values: List<Int>): List<String> {
    val labels = ArrayList<String>(values.size)
    for (value in values) {
        labels.add(stringResource(R.string.joist_section_option, value))
    }
    return labels
}

@Composable
private fun percentLabels(values: List<Int>): List<String> {
    val labels = ArrayList<String>(values.size)
    for (value in values) {
        labels.add(stringResource(R.string.percent_value, value))
    }
    return labels
}

@Composable
fun JoistSetupScreen(onBack: () -> Unit, onDrawPlan: () -> Unit) {
    val joistSetupViewModel: JoistSetupViewModel = viewModel(factory = AppViewModelFactory)
    val state by joistSetupViewModel.state.collectAsStateWithLifecycle()
    val report = state.report

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.joist_setup_title),
                subtitle = stringResource(R.string.joist_setup_subtitle),
                onBack = onBack,
                trailing = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(RustTint)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.step_two),
                            style = MaterialTheme.typography.labelSmall,
                            color = RustAccent
                        )
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                if (report != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, SecondarySage),
                        colors = CardDefaults.cardColors(containerColor = PaperSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            FramingCanvas(
                                layout = report.joists,
                                progress = 1f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.semantics {
                                    liveRegion = LiveRegionMode.Polite
                                },
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CounterChip(
                                    value = report.joists.joistCount.toString(),
                                    label = stringResource(R.string.unit_joists)
                                )
                                CounterChip(
                                    value = report.joists.beamLines.toString(),
                                    label = stringResource(R.string.unit_beams)
                                )
                                CounterChip(
                                    value = report.boards.rows.toString(),
                                    label = stringResource(R.string.unit_rows)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_joist),
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                    SectionHeader(title = stringResource(R.string.section_spacing))
                }
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = lengthOptionLabels(JoistOptions.spacing, state.unitSystem),
                    selectedIndex = state.spacingIndex,
                    onSelect = { index -> joistSetupViewModel.selectSpacing(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_direction))
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = listOf(
                        stringResource(R.string.direction_across),
                        stringResource(R.string.direction_along)
                    ),
                    selectedIndex = state.directionIndex,
                    onSelect = { index -> joistSetupViewModel.selectDirection(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_joist_size))
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = sectionLabels(JoistOptions.depth),
                    selectedIndex = state.depthIndex,
                    onSelect = { index -> joistSetupViewModel.selectDepth(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_board_width))
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = lengthOptionLabels(JoistOptions.boardWidth, state.unitSystem),
                    selectedIndex = state.boardWidthIndex,
                    onSelect = { index -> joistSetupViewModel.selectBoardWidth(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_board_gap))
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = millimetreLabels(JoistOptions.gap),
                    selectedIndex = state.gapIndex,
                    onSelect = { index -> joistSetupViewModel.selectGap(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_waste))
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = percentLabels(JoistOptions.waste),
                    selectedIndex = state.wasteIndex,
                    onSelect = { index -> joistSetupViewModel.selectWaste(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                NoteCard(
                    text = stringResource(R.string.note_spans),
                    spriteRes = R.drawable.sprite_5,
                    spriteDescription = stringResource(R.string.cd_tape_measure)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                PrimaryButton(
                    text = stringResource(R.string.draw_framing_plan),
                    onClick = onDrawPlan
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}
