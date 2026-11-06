package com.deckjoist.spacer.presentation.fasteners

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.CedarDeep
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.PaperOverlay
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.TextMuted
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.ui.PrimaryButton
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.ScreenHeader
import com.deckjoist.spacer.core.ui.SectionHeader
import com.deckjoist.spacer.core.ui.StatCard

@Composable
fun FastenerCountScreen(onBack: () -> Unit, onMaterialList: () -> Unit) {
    val fastenerCountViewModel: FastenerCountViewModel = viewModel(factory = AppViewModelFactory)
    val state by fastenerCountViewModel.state.collectAsStateWithLifecycle()
    val report = state.report

    ScreenBackground(overlay = PaperOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.fasteners_title),
                subtitle = stringResource(R.string.fasteners_subtitle),
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
                val tally = report.fasteners
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(
                            label = stringResource(R.string.stat_screws),
                            value = tally.screwsWithWaste.toString(),
                            accent = RustAccent,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_hangers),
                            value = tally.hangersWithWaste.toString(),
                            accent = CedarPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(
                            label = stringResource(R.string.stat_brackets),
                            value = tally.bracketsWithWaste.toString(),
                            accent = CedarDeep,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_structural),
                            value = tally.structuralWithWaste.toString(),
                            accent = CedarPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader(title = stringResource(R.string.section_breakdown))
                    Spacer(modifier = Modifier.height(12.dp))

                    BreakdownRow(
                        spriteRes = R.drawable.sprite_3,
                        spriteDescription = stringResource(R.string.cd_screw),
                        title = stringResource(R.string.breakdown_screws),
                        detail = stringResource(
                            R.string.breakdown_screws_maths,
                            tally.rows,
                            tally.joistCount,
                            tally.screws
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BreakdownRow(
                        spriteRes = R.drawable.sprite_4,
                        spriteDescription = stringResource(R.string.cd_hanger),
                        title = stringResource(R.string.breakdown_hangers),
                        detail = stringResource(
                            R.string.breakdown_hangers_maths,
                            tally.interiorJoists,
                            tally.hangers
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BreakdownRow(
                        spriteRes = R.drawable.sprite_4,
                        spriteDescription = stringResource(R.string.cd_bracket),
                        title = stringResource(R.string.breakdown_brackets),
                        detail = stringResource(
                            R.string.breakdown_brackets_maths,
                            tally.postCount,
                            tally.brackets
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, RustAccent.copy(alpha = 0.4f)),
                        colors = CardDefaults.cardColors(containerColor = PaperSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = stringResource(
                                    R.string.waste_line,
                                    tally.wastePercent,
                                    tally.screwsWithWaste
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = RustAccent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(
                                    R.string.waste_boxes,
                                    tally.screwBoxes,
                                    tally.screwBoxSize
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    PrimaryButton(
                        text = stringResource(R.string.add_to_material_list),
                        onClick = onMaterialList
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}

@Composable
private fun BreakdownRow(
    spriteRes: Int,
    spriteDescription: String,
    title: String,
    detail: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(spriteRes),
            contentDescription = spriteDescription,
            modifier = Modifier.size(28.dp),
            contentScale = ContentScale.Fit
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
