package com.deckjoist.spacer.presentation.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.PaperOverlay
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.core.theme.TextMuted
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.ui.NoteCard
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.ScreenHeader
import com.deckjoist.spacer.core.ui.SecondaryButton
import com.deckjoist.spacer.core.ui.SectionHeader
import com.deckjoist.spacer.core.ui.TicketRow

private val CHECKLIST = listOf(
    R.string.note_check_span,
    R.string.note_check_footing,
    R.string.note_check_fall,
    R.string.note_check_fasteners,
    R.string.note_check_expansion,
    R.string.note_check_dry_fit
)

@Composable
fun BuildNotesScreen(onBack: () -> Unit, onMenu: () -> Unit) {
    val buildNotesViewModel: BuildNotesViewModel = viewModel(factory = AppViewModelFactory)
    val state by buildNotesViewModel.state.collectAsStateWithLifecycle()

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.build_notes),
                subtitle = state.deckName,
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
                SectionHeader(
                    title = stringResource(R.string.section_rules_of_thumb),
                    subtitle = stringResource(R.string.section_rules_hint)
                )
                Spacer(modifier = Modifier.height(8.dp))
                for (index in CHECKLIST.indices) {
                    val ticked = state.checked.contains(index)
                    TicketRow(
                        label = stringResource(CHECKLIST[index]),
                        quantity = null,
                        ticked = ticked,
                        stateLabel = if (ticked) {
                            stringResource(R.string.state_ticked)
                        } else {
                            stringResource(R.string.state_not_ticked)
                        },
                        onToggle = { buildNotesViewModel.toggleChecklistItem(index) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(title = stringResource(R.string.section_your_notes))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = { value -> buildNotesViewModel.setNotes(value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    minLines = 5,
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    label = {
                        Text(
                            text = stringResource(R.string.field_notes),
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
                Spacer(modifier = Modifier.height(16.dp))
                NoteCard(
                    text = stringResource(R.string.note_disclaimer),
                    spriteRes = R.drawable.sprite_5,
                    spriteDescription = stringResource(R.string.cd_tape_measure),
                    spriteSize = 32
                )
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
