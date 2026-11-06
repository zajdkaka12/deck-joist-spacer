package com.deckjoist.spacer.presentation.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deckjoist.spacer.R
import com.deckjoist.spacer.core.di.AppViewModelFactory
import com.deckjoist.spacer.core.theme.CedarPrimary
import com.deckjoist.spacer.core.theme.MenuOverlay
import com.deckjoist.spacer.core.theme.PaperSurface
import com.deckjoist.spacer.core.theme.RustAccent
import com.deckjoist.spacer.core.theme.RustTint
import com.deckjoist.spacer.core.theme.SecondarySage
import com.deckjoist.spacer.core.theme.TextMuted
import com.deckjoist.spacer.core.theme.TextPrimary
import com.deckjoist.spacer.core.theme.TextSecondary
import com.deckjoist.spacer.core.ui.PrimaryButton
import com.deckjoist.spacer.core.ui.ScreenBackground
import com.deckjoist.spacer.core.ui.SecondaryButton
import com.deckjoist.spacer.core.ui.StatCard
import com.deckjoist.spacer.domain.model.UnitSystem
import com.deckjoist.spacer.core.ui.deckSizeText
import com.deckjoist.spacer.core.ui.shortLengthText
import com.deckjoist.spacer.presentation.dialog.DeleteDeckDialog

@Composable
fun MenuScreen(
    onStartNewDeck: () -> Unit,
    onOpenDeck: () -> Unit,
    onUnitOptions: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    val pending = state.pendingDelete
    if (pending != null) {
        DeleteDeckDialog(
            deckName = pending.name,
            onConfirm = { menuViewModel.confirmDelete() },
            onDismiss = { menuViewModel.dismissDelete() }
        )
    }

    ScreenBackground(overlay = MenuOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_home_mark),
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onUnitOptions, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.cd_unit_options),
                        tint = CedarPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.menu_hero_top),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 40.sp,
                color = TextPrimary
            )
            Text(
                text = stringResource(R.string.menu_hero_bottom),
                style = MaterialTheme.typography.headlineMedium,
                color = CedarPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(2.dp)
                    .background(RustAccent, RoundedCornerShape(1.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(18.dp))
            if (state.rows.isNotEmpty() && state.totalBoards > 0) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        label = stringResource(R.string.stat_saved_decks),
                        value = state.rows.size.toString(),
                        accent = CedarPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_total_boards),
                        value = state.totalBoards.toString(),
                        accent = RustAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.rows.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_joist),
                        modifier = Modifier
                            .size(96.dp)
                            .alpha(0.4f),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = stringResource(R.string.menu_empty_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.menu_empty_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = state.rows, key = { row -> row.plan.id }) { row ->
                        DeckRowCard(
                            row = row,
                            unitSystem = state.unitSystem,
                            onOpen = {
                                menuViewModel.openDeck(row.plan)
                                onOpenDeck()
                            },
                            onDelete = { menuViewModel.requestDelete(row.plan) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            PrimaryButton(
                text = stringResource(R.string.start_new_deck),
                icon = ImageVector.vectorResource(R.drawable.ic_add),
                onClick = {
                    menuViewModel.startNewDeck()
                    onStartNewDeck()
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.unit_options),
                icon = ImageVector.vectorResource(R.drawable.ic_settings),
                modifier = Modifier.fillMaxWidth(),
                onClick = onUnitOptions
            )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun DeckRowCard(
    row: MenuDeckRow,
    unitSystem: UnitSystem,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(row.plan.id) {
        entered = true
    }

    AnimatedVisibility(
        visible = entered,
        enter = fadeIn(tween(260)) + slideInHorizontally(tween(260)) { full -> -full / 6 }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpen),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, SecondarySage),
            colors = CardDefaults.cardColors(containerColor = PaperSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 6.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = row.plan.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = stringResource(
                            R.string.deck_card_meta,
                            deckSizeText(row.plan.lengthMm, row.plan.widthMm, unitSystem),
                            shortLengthText(row.plan.spacingMm, unitSystem)
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(RustTint)
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.deck_card_boards, row.boardCount),
                            style = MaterialTheme.typography.labelSmall,
                            color = RustAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = stringResource(R.string.cd_boards),
                    modifier = Modifier.size(44.dp),
                    contentScale = ContentScale.Fit
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.cd_delete_deck),
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
