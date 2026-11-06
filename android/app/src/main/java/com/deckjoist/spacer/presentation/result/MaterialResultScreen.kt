package com.deckjoist.spacer.presentation.result

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.ResultOverlay
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.TextMuted
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.theme.TextSecondary
import com.deckjoist.spacer.core.ui.PrimaryButton
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.SecondaryButton
import com.deckjoist.spacer.core.ui.StatCard
import com.deckjoist.spacer.core.ui.TicketRow
import com.deckjoist.spacer.core.ui.deckSizeText
import com.deckjoist.spacer.domain.model.MaterialItem
import com.deckjoist.spacer.domain.model.MaterialKind

@Composable
private fun materialLabel(item: MaterialItem): String = when (item.kind) {
    MaterialKind.JOIST -> stringResource(
        R.string.item_joist,
        item.depthMm,
        item.widthMm,
        item.lengthMm
    )

    MaterialKind.RIM_JOIST -> stringResource(
        R.string.item_rim_joist,
        item.depthMm,
        item.widthMm,
        item.lengthMm
    )

    MaterialKind.BEAM -> stringResource(
        R.string.item_beam,
        item.depthMm,
        item.widthMm,
        item.lengthMm
    )

    MaterialKind.DECK_BOARD -> stringResource(
        R.string.item_deck_board,
        item.depthMm,
        item.widthMm,
        item.lengthMm
    )

    MaterialKind.POST -> stringResource(
        R.string.item_post,
        item.depthMm,
        item.widthMm,
        item.lengthMm
    )

    MaterialKind.JOIST_HANGER -> stringResource(R.string.item_hanger, item.depthMm)
    MaterialKind.POST_BRACKET -> stringResource(R.string.item_bracket)
    MaterialKind.DECK_SCREW -> stringResource(R.string.item_deck_screw, item.depthMm)
    MaterialKind.STRUCTURAL_SCREW -> stringResource(R.string.item_structural_screw, item.depthMm)
}

@Composable
fun MaterialResultScreen(
    onBack: () -> Unit,
    onPlanAgain: () -> Unit,
    onNotes: () -> Unit,
    onMenu: () -> Unit
) {
    val materialResultViewModel: MaterialResultViewModel = viewModel(factory = AppViewModelFactory)
    val state by materialResultViewModel.state.collectAsStateWithLifecycle()
    val report = state.report

    LaunchedEffect(Unit) {
        materialResultViewModel.events.collect { event ->
            when (event) {
                MaterialResultEvent.PlanStored -> onPlanAgain()
            }
        }
    }

    ScreenBackground(overlay = ResultOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.result_title),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 32.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(2.dp)
                    .background(RustAccent, RoundedCornerShape(1.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (report != null) {
                Text(
                    text = stringResource(
                        R.string.result_subtitle,
                        report.plan.name,
                        deckSizeText(report.plan.lengthMm, report.plan.widthMm, state.unitSystem),
                        report.plan.wastePercent
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(14.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(items = report.materials, key = { item -> item.kind.name }) { item ->
                        val ticked = state.ticked.contains(item.kind)
                        TicketRow(
                            label = materialLabel(item),
                            quantity = item.quantity.toString(),
                            ticked = ticked,
                            stateLabel = if (ticked) {
                                stringResource(R.string.state_ticked)
                            } else {
                                stringResource(R.string.state_not_ticked)
                            },
                            onToggle = { materialResultViewModel.toggleTicked(item.kind) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_lineal_metres),
                        value = stringResource(R.string.metres_value, report.linealMetres),
                        accent = CedarPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_items_ticked),
                        value = stringResource(
                            R.string.ticked_of_total,
                            state.ticked.size,
                            report.materials.size
                        ),
                        accent = RustAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.result_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PrimaryButton(
                    text = stringResource(R.string.save_and_plan_again),
                    onClick = { materialResultViewModel.storeAndPlanAgain() }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SecondaryButton(
                        text = stringResource(R.string.build_notes),
                        modifier = Modifier.weight(1f),
                        onClick = onNotes
                    )
                    SecondaryButton(
                        text = stringResource(R.string.menu),
                        modifier = Modifier.weight(1f),
                        onClick = onMenu
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.state_no_plan),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted
                    )
                }
                SecondaryButton(
                    text = stringResource(R.string.back_to_plan),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onBack
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}
