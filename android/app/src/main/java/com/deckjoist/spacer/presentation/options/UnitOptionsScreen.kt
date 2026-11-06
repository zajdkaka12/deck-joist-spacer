package com.deckjoist.spacer.presentation.options

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.deckjoist.spacer.core.theme.MossBackground
import com.deckjoist.spacer.core.theme.PaperOverlay
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.core.theme.TextMuted
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.ScreenHeader
import com.deckjoist.spacer.core.ui.SecondaryButton
import com.deckjoist.spacer.core.ui.SectionHeader
import com.deckjoist.spacer.core.ui.SegmentedChips
import com.deckjoist.spacer.core.ui.lengthText
import com.deckjoist.spacer.domain.model.UnitSystem
import com.deckjoist.spacer.presentation.joist.JoistOptions

@Composable
fun UnitOptionsScreen(onBack: () -> Unit, onMenu: () -> Unit) {
    val unitOptionsViewModel: UnitOptionsViewModel = viewModel(factory = AppViewModelFactory)
    val state by unitOptionsViewModel.state.collectAsStateWithLifecycle()

    val wasteLabels = ArrayList<String>(JoistOptions.waste.size)
    for (value in JoistOptions.waste) {
        wasteLabels.add(stringResource(R.string.percent_value, value))
    }

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.unit_options),
                subtitle = stringResource(R.string.unit_options_subtitle),
                onBack = onBack
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(title = stringResource(R.string.section_units))
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = listOf(
                        stringResource(R.string.units_metric),
                        stringResource(R.string.units_imperial)
                    ),
                    selectedIndex = if (state.unitSystem == UnitSystem.METRIC) 0 else 1,
                    onSelect = { index -> unitOptionsViewModel.selectUnitSystem(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_default_waste))
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedChips(
                    options = wasteLabels,
                    selectedIndex = state.wasteIndex,
                    onSelect = { index -> unitOptionsViewModel.selectWaste(index) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_behaviour))
                Spacer(modifier = Modifier.height(10.dp))
                SwitchRow(
                    label = stringResource(R.string.toggle_deltas),
                    checked = state.showOnCentreDeltas,
                    onCheckedChange = { value ->
                        unitOptionsViewModel.setShowOnCentreDeltas(value)
                    }
                )
                SwitchRow(
                    label = stringResource(R.string.toggle_haptic),
                    checked = state.hapticOnSnap,
                    onCheckedChange = { value -> unitOptionsViewModel.setHapticOnSnap(value) }
                )
                Spacer(modifier = Modifier.height(18.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, SecondarySage),
                    colors = CardDefaults.cardColors(containerColor = PaperSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = stringResource(R.string.preview_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(
                                R.string.preview_conversion,
                                lengthText(state.previewMm, UnitSystem.METRIC),
                                lengthText(state.previewMm, UnitSystem.IMPERIAL)
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = RustAccent
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onMenu
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MossBackground,
                checkedTrackColor = CedarPrimary,
                uncheckedThumbColor = SecondarySage,
                uncheckedTrackColor = PaperSurface,
                uncheckedBorderColor = SecondarySage
            )
        )
    }
}
