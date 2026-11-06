package com.deckjoist.spacer.presentation.marks

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.PaperOverlay
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.PaperSurfaceAlt
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.RustTint
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.core.theme.TextMuted
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.ScreenHeader
import com.deckjoist.spacer.core.ui.SecondaryButton
import com.deckjoist.spacer.core.ui.lengthText

@Composable
fun LayoutMarksScreen(onBack: () -> Unit) {
    val layoutMarksViewModel: LayoutMarksViewModel = viewModel(factory = AppViewModelFactory)
    val state by layoutMarksViewModel.state.collectAsStateWithLifecycle()
    val report = state.report

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.marks_title),
                subtitle = stringResource(R.string.marks_subtitle),
                onBack = onBack
            )
            if (report == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.state_no_plan),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    itemsIndexed(report.joists.marksMm) { index, mark ->
                        val delta = if (index == 0) 0 else mark - report.joists.marksMm[index - 1]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(
                                    if (index % 2 == 0) PaperSurfaceAlt.copy(alpha = 0.45f)
                                    else Color.Transparent
                                )
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(RustTint, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = RustAccent
                                )
                            }
                            Text(
                                text = lengthText(mark, state.unitSystem),
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            if (state.showDeltas && delta > 0) {
                                Text(
                                    text = stringResource(R.string.mark_delta, delta),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CedarPrimary
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, SecondarySage),
                        colors = CardDefaults.cardColors(containerColor = PaperSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = stringResource(
                                    R.string.marks_last,
                                    lengthText(
                                        report.joists.marksMm.lastOrNull() ?: 0,
                                        state.unitSystem
                                    )
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(
                                    R.string.marks_outside_face,
                                    lengthText(report.joists.runMm, state.unitSystem)
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
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
}
