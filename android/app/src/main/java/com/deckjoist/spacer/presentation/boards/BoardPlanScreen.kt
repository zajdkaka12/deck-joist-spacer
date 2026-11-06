package com.deckjoist.spacer.presentation.boards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.deckjoist.spacer.core.ui.lengthText
import com.deckjoist.spacer.core.ui.shortLengthText

@Composable
fun BoardPlanScreen(onBack: () -> Unit) {
    val boardPlanViewModel: BoardPlanViewModel = viewModel(factory = AppViewModelFactory)
    val state by boardPlanViewModel.state.collectAsStateWithLifecycle()
    val report = state.report

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.boards_title),
                subtitle = if (report != null) {
                    stringResource(
                        R.string.boards_subtitle,
                        shortLengthText(report.boards.boardWidthMm, state.unitSystem),
                        report.boards.gapMm
                    )
                } else {
                    null
                },
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
                val rows = report.boards.rows
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    val pitch = size.height / rows.toFloat()
                    val gapPx = (pitch * report.boards.gapMm / report.boards.pitchMm.toFloat())
                        .coerceIn(1f, pitch * 0.5f)
                    for (index in 0 until rows) {
                        val last = index == rows - 1
                        val top = index * pitch
                        drawRect(
                            color = if (last && report.boards.needsRip) RustAccent else CedarPrimary,
                            topLeft = Offset(0f, top),
                            size = Size(size.width, (pitch - gapPx).coerceAtLeast(1f))
                        )
                    }
                }

                val entries = remember(report) {
                    (1..report.boards.rows).map { number ->
                        val width = if (number == report.boards.rows) {
                            report.boards.lastBoardWidthMm
                        } else {
                            report.boards.boardWidthMm
                        }
                        BoardRowEntry(number, width, number == report.boards.rows && report.boards.needsRip)
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(items = entries, key = { entry -> entry.number }) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(
                                    if (entry.ripped) PaperSurface else MossBackground.copy(alpha = 0.5f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 3.dp, height = 24.dp)
                                    .background(
                                        if (entry.ripped) RustAccent else CedarPrimary,
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                            Text(
                                text = stringResource(R.string.board_row, entry.number),
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            if (entry.ripped) {
                                Image(
                                    painter = painterResource(R.drawable.sprite_3),
                                    contentDescription = stringResource(R.string.cd_screw),
                                    modifier = Modifier.size(28.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Text(
                                text = shortLengthText(entry.widthMm, state.unitSystem),
                                style = MaterialTheme.typography.titleMedium,
                                color = if (entry.ripped) RustAccent else CedarPrimary
                            )
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.sprite_2),
                                contentDescription = stringResource(R.string.cd_boards),
                                modifier = Modifier.size(36.dp),
                                contentScale = ContentScale.Fit
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(
                                        R.string.boards_summary,
                                        report.boards.rows,
                                        report.boards.piecesPerRow
                                    ),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(
                                        R.string.boards_last,
                                        lengthText(report.boards.lastBoardWidthMm, state.unitSystem)
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
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

private data class BoardRowEntry(
    val number: Int,
    val widthMm: Int,
    val ripped: Boolean
)
