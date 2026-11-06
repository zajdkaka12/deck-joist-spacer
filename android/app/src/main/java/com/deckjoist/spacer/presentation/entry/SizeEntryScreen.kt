package com.deckjoist.spacer.presentation.entry

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
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
import com.deckjoist.spacer.core.theme.TextMuted
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.ui.PrimaryButton
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.ScreenHeader
import com.deckjoist.spacer.core.ui.SectionHeader
import com.deckjoist.spacer.core.ui.StepperRow
import com.deckjoist.spacer.core.ui.areaText
import com.deckjoist.spacer.core.ui.lengthText

@Composable
fun SizeEntryScreen(onBack: () -> Unit, onNext: () -> Unit) {
    val sizeEntryViewModel: SizeEntryViewModel = viewModel(factory = AppViewModelFactory)
    val state by sizeEntryViewModel.state.collectAsStateWithLifecycle()

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.size_entry_title),
                subtitle = stringResource(R.string.size_entry_subtitle),
                onBack = onBack,
                trailing = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(RustTint)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.step_one),
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
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(
                    title = stringResource(R.string.section_footprint),
                    subtitle = stringResource(R.string.section_footprint_hint)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, SecondarySage),
                    colors = CardDefaults.cardColors(containerColor = PaperSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        StepperRow(
                            label = stringResource(R.string.field_length),
                            value = lengthText(state.lengthMm, state.unitSystem),
                            readout = lengthText(state.lengthMm, state.unitSystem),
                            canDecrement = state.canDecreaseLength,
                            canIncrement = state.canIncreaseLength,
                            onDecrement = { sizeEntryViewModel.stepLength(false) },
                            onIncrement = { sizeEntryViewModel.stepLength(true) }
                        )
                        Divider()
                        StepperRow(
                            label = stringResource(R.string.field_width),
                            value = lengthText(state.widthMm, state.unitSystem),
                            readout = lengthText(state.widthMm, state.unitSystem),
                            canDecrement = state.canDecreaseWidth,
                            canIncrement = state.canIncreaseWidth,
                            onDecrement = { sizeEntryViewModel.stepWidth(false) },
                            onIncrement = { sizeEntryViewModel.stepWidth(true) }
                        )
                        Divider()
                        StepperRow(
                            label = stringResource(R.string.field_height),
                            value = lengthText(state.heightMm, state.unitSystem),
                            readout = lengthText(state.heightMm, state.unitSystem),
                            canDecrement = state.canDecreaseHeight,
                            canIncrement = state.canIncreaseHeight,
                            onDecrement = { sizeEntryViewModel.stepHeight(false) },
                            onIncrement = { sizeEntryViewModel.stepHeight(true) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = stringResource(R.string.section_deck_name))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { value -> sizeEntryViewModel.setName(value) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.titleMedium,
                    label = {
                        Text(
                            text = stringResource(R.string.field_deck_name),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CedarPrimary,
                        unfocusedBorderColor = SecondarySage,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = CedarPrimary,
                        unfocusedLabelColor = TextMuted,
                        cursorColor = RustAccent,
                        focusedContainerColor = PaperSurface,
                        unfocusedContainerColor = PaperSurface
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_deck_area),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = areaText(state.lengthMm, state.widthMm, state.unitSystem),
                        style = MaterialTheme.typography.titleMedium,
                        color = RustAccent
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(R.string.size_entry_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PrimaryButton(
                    text = stringResource(R.string.next_joist_setup),
                    enabled = state.canContinue,
                    onClick = onNext
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(SecondarySage.copy(alpha = 0.6f))
    )
}
